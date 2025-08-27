package com.inmobiliaria.backend.service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.inmobiliaria.backend.dto.ConceptoRequest;
import com.inmobiliaria.backend.dto.ConceptoResponse;
import com.inmobiliaria.backend.dto.MedioPagoRequest;
import com.inmobiliaria.backend.dto.MedioPagoResponse;
import com.inmobiliaria.backend.dto.ReciboRequest;
import com.inmobiliaria.backend.dto.ReciboResponse;
import com.inmobiliaria.backend.exception.AdminNoEncontradoException;
import com.inmobiliaria.backend.exception.ClienteNoEncontradoException;
import com.inmobiliaria.backend.exception.PropiedadNoEncontradaException;
import com.inmobiliaria.backend.exception.ReciboNoEncontradoException;
import com.inmobiliaria.backend.model.Cliente;
import com.inmobiliaria.backend.model.Concepto;
import com.inmobiliaria.backend.model.MedioPago;
import com.inmobiliaria.backend.model.Propiedad;
import com.inmobiliaria.backend.model.Recibo;
import com.inmobiliaria.backend.model.Usuario;
import com.inmobiliaria.backend.repository.ClienteRepository;
import com.inmobiliaria.backend.repository.PropiedadRepository;
import com.inmobiliaria.backend.repository.ReciboRepository;
import com.inmobiliaria.backend.repository.UsuarioRepository;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReciboService {

    private final UsuarioRepository usuarioRepository;
    private final ReciboRepository reciboRepository;
    private final ClienteRepository clienteRepository;
    private final PropiedadRepository propiedadRepository;

    public ReciboResponse crearRecibo(ReciboRequest request) throws AdminNoEncontradoException, IOException, ClienteNoEncontradoException, PropiedadNoEncontradaException {
        if (request.getConceptos() == null || request.getConceptos().isEmpty()) {
            throw new IllegalArgumentException("La lista de conceptos no puede estar vacía.");
        }
        if (request.getMediosPagos() == null || request.getMediosPagos().isEmpty()) {
            throw new IllegalArgumentException("La lista de medios de pago no puede estar vacía.");
        }

        // buscar Cliente y Propiedad por id
        Cliente cliente = clienteRepository.findById(request.getClienteId())
                .orElseThrow(() -> new ClienteNoEncontradoException("Cliente con ID " + request.getClienteId() + " no encontrado."));
        Propiedad propiedad = propiedadRepository.findById(request.getPropiedadId())
                .orElseThrow(() -> new PropiedadNoEncontradaException("Propiedad con ID " + request.getPropiedadId() + " no encontrada."));

        Usuario usuario = usuarioRepository.findByUsername("admin")
                .orElseThrow(() -> new AdminNoEncontradoException("Usuario admin no encontrado."));

        List<Concepto> conceptosEntidad = request.getConceptos().stream()
                .map(this::mapearConcepto)
                .collect(Collectors.toList());

        List<MedioPago> mediosPagosEntidad = request.getMediosPagos().stream()
                .map(this::mapearMedioPago)
                .collect(Collectors.toList());

        Recibo recibo = construirRecibo(request, usuario, conceptosEntidad, mediosPagosEntidad, cliente, propiedad);

        recibo = reciboRepository.save(recibo);

        recibo.setNumeroRecibo(recibo.getId());
        recibo = reciboRepository.save(recibo);

        generarPdfRecibo(recibo);

        return mapearAResponse(recibo);
    }

    private void generarPdfRecibo(Recibo recibo) throws IOException {
        String htmlTemplate = Files.readString(Paths.get("src/main/resources/templates/recibo_template.html"));
        
        String htmlFinal = reemplazarPlaceholders(htmlTemplate, recibo);

        System.out.println("HTML generado:\n" + htmlFinal);

        String outputPdfPath = "src/main/resources/PDF_Recibos/recibo_" + recibo.getNumeroRecibo() + ".pdf";

        try (OutputStream os = new FileOutputStream(outputPdfPath)) {
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.withHtmlContent(htmlFinal, Paths.get("src/main/resources/templates").toAbsolutePath().toUri().toString());
            builder.toStream(os);
            builder.run();
        }
    }

    private String reemplazarPlaceholders(String html, Recibo recibo) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        DecimalFormat df = new DecimalFormat("#,##0.00");

        html = html.replace("Nº 0001", "Nº " + recibo.getNumeroRecibo());
        html = html.replace("Fecha: 00/00/0000", "Fecha: " + sdf.format(recibo.getFechaRecibo()));
        
        //cliente
        html = html.replace("clienteApellido, clienteNombre", escapeCustom(recibo.getCliente().getNombre()));
        html = html.replace("clienteDireccion", escapeCustom(recibo.getCliente().getDireccion()));
        html = html.replace("clienteIVA", escapeCustom(recibo.getCliente().getIva()));
        html = html.replace("cuitCliente", escapeCustom(recibo.getCliente().getCuit()));
        html = html.replace("localidadCliente", escapeCustom(recibo.getCliente().getLocalidad()));

        //propiedad
        html = html.replace("contratoNumero", escapeCustom(recibo.getPropiedad().getNumContrato()));
        html = html.replace("propiedadCalle", escapeCustom(recibo.getPropiedad().getDireccionPropietario()));
        html = html.replace("propiedadLocalidad", escapeCustom(recibo.getPropiedad().getLocalidadPropiedad()));
        html = html.replace("contratoInicio", sdf.format(recibo.getPropiedad().getInicioContrato()));
        html = html.replace("contratoFin", sdf.format(recibo.getPropiedad().getFinContrato()));
        html = html.replace("propietarioNombre", escapeCustom(recibo.getPropiedad().getNombrePropietario()));
        html = html.replace("propietarioCuit", escapeCustom(recibo.getPropiedad().getCuitPropietario()));

        // onceptos
        StringBuilder conceptosHtml = new StringBuilder();
        StringBuilder periodosHtml = new StringBuilder();
        StringBuilder aniosHtml = new StringBuilder();
        StringBuilder importesHtml = new StringBuilder();

        for (Concepto concepto : recibo.getConceptos()) {
            conceptosHtml.append("<p>").append(escapeCustom(concepto.getConcepto())).append("</p>");
            periodosHtml.append("<p>").append(concepto.getPeriodo() != null ? escapeCustom(concepto.getPeriodo()) : "-").append("</p>");
            aniosHtml.append("<p>").append(concepto.getAnio() != null ? escapeCustom(concepto.getAnio()) : "-").append("</p>");
            importesHtml.append("<p>$ ").append(concepto.getImporte() != null ? df.format(concepto.getImporte()) : "-").append("</p>");
        }
        html = html.replace("<p>conceptos</p>", conceptosHtml.toString());
        html = html.replace("<p>periodos</p>", periodosHtml.toString());
        html = html.replace("<p>años</p>", aniosHtml.toString());
        html = html.replace("<p>importes</p>", importesHtml.toString());

        html = html.replace("subtotal", df.format(recibo.getSubtotal()));

        //Medios de Pagos
        StringBuilder mediosPagosHtml = new StringBuilder();
        for (MedioPago medioPago : recibo.getMediosPagos()) {
            String medio = escapeCustom(medioPago.getMedioPago());
            String importe = df.format(medioPago.getImportePago());
            int dotsLength = 60 - medio.length() - importe.length();
            String dots = ".".repeat(Math.max(0, dotsLength));
            mediosPagosHtml.append("<span>").append(medio).append(" ").append(dots).append(" ").append(importe).append("</span><br/>");
        }
        html = html.replace("<span>mediosPagos</span>", mediosPagosHtml.toString());

        html = html.replace("Total Recibo: $ totalRecibo", "Total Recibo: $ " + df.format(recibo.getTotal()));
        html = html.replace("Son pesos: pesos", "Son pesos: " + escapeCustom(recibo.getPesos()));

        return html;
    }

    private Recibo construirRecibo(ReciboRequest request, Usuario usuario, List<Concepto> conceptos, List<MedioPago> medioPagos, Cliente cliente, Propiedad propiedad) {
        double subtotal = conceptos.stream()
                .mapToDouble(concepto -> concepto.getImporte() != null ? concepto.getImporte() : 0.0)
                .sum();

        double total = medioPagos.stream()
                .mapToDouble(medioPago -> medioPago.getImportePago() != null ? medioPago.getImportePago() : 0.0)
                .sum();

        return Recibo.builder()
                .usuario(usuario)
                .fechaRecibo(new Date())
                .conceptos(conceptos)
                .cliente(cliente)
                .propiedad(propiedad)
                .subtotal(subtotal)
                .mediosPagos(medioPagos)
                .total(total)
                .pesos(request.getPesos())
                .build();
    }

    private Concepto mapearConcepto(ConceptoRequest request) {
        return Concepto.builder()
                .concepto(request.getConcepto())
                .periodo(request.getPeriodo())
                .anio(request.getAnio())
                .importe(request.getImporte() != null ? request.getImporte() : 0.0)
                .build();
    }

    private MedioPago mapearMedioPago(MedioPagoRequest request) {
        return MedioPago.builder()
                .medioPago(request.getMedioPago())
                .importePago(request.getImportePago())
                .build();
    }

    private ReciboResponse mapearAResponse(Recibo recibo) {
        return ReciboResponse.builder()
                .numeroRecibo(recibo.getNumeroRecibo())
                .fechaRecibo(recibo.getFechaRecibo())
                .conceptos(mapearConceptosAResponse(recibo.getConceptos()))
                .subtotal(recibo.getSubtotal())
                .mediosPagos(mapearMediosPagosAResponse(recibo.getMediosPagos()))
                .total(recibo.getTotal())
                .pesos(recibo.getPesos())
                .clienteId(recibo.getCliente().getId())
                .propiedadId(recibo.getPropiedad().getId())
                .build();
    }

    private List<ConceptoResponse> mapearConceptosAResponse(List<Concepto> conceptos) {
        return conceptos.stream()
                .map(c -> ConceptoResponse.builder()
                        .concepto(c.getConcepto())
                        .periodo(c.getPeriodo())
                        .anio(c.getAnio())
                        .importe(c.getImporte())
                        .build())
                .collect(Collectors.toList());
    }

    private List<MedioPagoResponse> mapearMediosPagosAResponse(List<MedioPago> mediosPagos) {
        return mediosPagos.stream()
                .map(m -> MedioPagoResponse.builder()
                        .medioPago(m.getMedioPago())
                        .importePago(m.getImportePago())
                        .build())
                .collect(Collectors.toList());
    }

    private String escapeCustom(String input) {
        if (input == null) return "";
        return input.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }

    public List<ReciboResponse> listarTodos() {
        return reciboRepository.findAll().stream()
                .map(this::mapearAResponse)
                .collect(Collectors.toList());
    }

    public ReciboResponse obtenerRecibo(Integer numRecibo) throws ReciboNoEncontradoException {
        Recibo recibo = reciboRepository.findById(numRecibo)
                .orElseThrow(() -> new ReciboNoEncontradoException("Recibo con número " + numRecibo + " no encontrado."));
        return mapearAResponse(recibo);
    }
}