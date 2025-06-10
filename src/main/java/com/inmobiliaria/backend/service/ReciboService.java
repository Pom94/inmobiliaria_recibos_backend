package com.inmobiliaria.backend.service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
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
import com.inmobiliaria.backend.model.Concepto;
import com.inmobiliaria.backend.model.MedioPago;
import com.inmobiliaria.backend.model.Recibo;
import com.inmobiliaria.backend.model.Usuario;
import com.inmobiliaria.backend.repository.ReciboRepository;
import com.inmobiliaria.backend.repository.UsuarioRepository;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReciboService {

    private final UsuarioRepository usuarioRepository;
    private final ReciboRepository reciboRepository;

    public ReciboResponse crearRecibo(ReciboRequest request) throws AdminNoEncontradoException, IOException{
        Usuario usuario = usuarioRepository.findByUsername("admin")
        .orElseThrow(() -> new AdminNoEncontradoException("Usuario admin no encontrado."));

        List<Concepto> conceptosEntidad = request.getConceptos().stream()
            .map(this::mapearConcepto)
            .collect(Collectors.toList());

        List<MedioPago> mediosPagosEntidad = request.getMediosPagos().stream()
            .map(this::mapearMedioPago)
            .collect(Collectors.toList());

        Recibo recibo = construirRecibo(request, usuario, conceptosEntidad, mediosPagosEntidad);

        recibo = reciboRepository.save(recibo);

        recibo.setNumeroRecibo(recibo.getId());

        recibo = reciboRepository.save(recibo);

        generarPdfRecibo(recibo);

        return mapearAResponse(recibo);

    }

    private void generarPdfRecibo(Recibo recibo) throws IOException {
        String htmlTemplate = Files.readString(Paths.get("src/main/resources/templates/recibo_template.html"), StandardCharsets.UTF_8);
        
        String htmlFinal = reemplazarPlaceholders(htmlTemplate, recibo);

        ///////
        System.out.println("HTML generado:\n" + htmlFinal);

        String outputPdfPath = "src/main/resources/PDF_Recibos/recibo_" + recibo.getNumeroRecibo() + ".pdf";

        try (OutputStream os = new FileOutputStream(outputPdfPath)){
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
        
        //Cliente
        html = html.replace("clienteApellido, clienteNombre", escapeCustom(recibo.getCliente()));
        html = html.replace("clienteDireccion", escapeCustom(recibo.getDireccionCliente()));
        html = html.replace("clienteIVA", escapeCustom(recibo.getIvaCliente()));
        html = html.replace("cuitCliente", escapeCustom(recibo.getCuitCliente()));
        html = html.replace("localidadCliente", escapeCustom(recibo.getLocalidadCliente()));

        //Contrato
        html = html.replace("contratoNumero", escapeCustom(recibo.getNumContrato()));
        html = html.replace("propiedadCalle", escapeCustom(recibo.getCallePropiedad()));
        html = html.replace("propiedadLocalidad", escapeCustom(recibo.getLocalidadPropiedad()));
        html = html.replace("contratoInicio", sdf.format(recibo.getInicioContrato()));
        html = html.replace("contratoFin", sdf.format(recibo.getFinContrato()));
        html = html.replace("propietarioNombre", escapeCustom(recibo.getPropietario()));
        html = html.replace("propietarioCuit", escapeCustom(recibo.getCuitPropietario()));

        //Conceptos
        StringBuilder conceptosHtml = new StringBuilder();
        StringBuilder periodosHtml = new StringBuilder();
        StringBuilder aniosHtml = new StringBuilder();
        StringBuilder importesHtml = new StringBuilder();

        for (Concepto concepto : recibo.getConceptos()){
            conceptosHtml.append("<p>").append(escapeCustom(concepto.getConcepto())).append("</p>");
            periodosHtml.append("<p>").append(concepto.getPeriodo() != null ? escapeCustom(concepto.getPeriodo()) : "-").append("</p>");
            aniosHtml.append("<p>").append(concepto.getAnio() != null ? escapeCustom(concepto.getAnio()) : "-").append("</p>");
            importesHtml.append("<p>$ ").append(concepto.getImporte() != null ? df.format(concepto.getImporte()) : "-").append("</p>");

        }
        html = html.replace("<p>conceptos</p>", conceptosHtml.toString());

        html = html.replace("<p>periodos</p>",periodosHtml.toString());

        html = html.replace("<p>años</p>", aniosHtml.toString());

        html = html.replace("<p>importes</p>", importesHtml.toString());

        html = html.replace("subtotal", df.format(recibo.getSubtotal()));

        //Medios de Pagos
        StringBuilder mediosPagosHtml = new StringBuilder();
        for (MedioPago medioPago : recibo.getMediosPagos()) {
            String medio = escapeCustom(medioPago.getMedioPago());
            String importe = df.format(medioPago.getImportePago());
            // Calcula los puntos suspensivos para alinear el importe
            int dotsLength = 60 - medio.length() - importe.length();
            String dots = ".".repeat(Math.max(0, dotsLength));
            mediosPagosHtml.append("<span>").append(medio).append(" ").append(dots).append(" ").append(importe).append("</span><br/>");
        }
        html = html.replace("<span>mediosPagos</span>", mediosPagosHtml.toString());


        html = html.replace("Total Recibo: $ totalRecibo", "Total Recibo: $ " + df.format(recibo.getTotal()));

        html = html.replace("Son pesos: pesos", "Son pesos: " + escapeCustom(recibo.getPesos()));

        return html;
    }

    public List<ReciboResponse> listarTodos() {
        return reciboRepository.findAll().stream()
        .map(this::mapearAResponse)
        .collect(Collectors.toList());
    }

    private Recibo construirRecibo(ReciboRequest request, Usuario usuario, List<Concepto> conceptos, List<MedioPago> medioPagos){
        return Recibo.builder()
        .usuario(usuario)
        .fechaRecibo(new Date())
        .conceptos(conceptos)
        .cliente(request.getCliente())
        .direccionCliente(request.getDireccionCliente())
        .ivaCliente(request.getIvaCliente())
        .cuitCliente(request.getCuitCliente())
        .localidadCliente(request.getLocalidadCliente())
        .numContrato(request.getNumContrato())
        .inicioContrato(request.getInicioContrato())
        .finContrato(request.getFinContrato())
        .propietario(request.getPropietario())
        .callePropiedad(request.getCallePropiedad())
        .localidadPropiedad(request.getLocalidadPropiedad())
        .cuitPropietario(request.getCuitPropietario())
        .subtotal(request.getSubtotal())
        .mediosPagos(medioPagos)
        .total(request.getTotal())
        .pesos(request.getPesos())
        .build();
    }

    private Concepto mapearConcepto(ConceptoRequest request){
        return Concepto.builder()
                    .concepto(request.getConcepto())
                    .periodo(request.getPeriodo())
                    .anio(request.getAnio())
                    .importe(request.getImporte())
                    .build();
    }
    private MedioPago mapearMedioPago(MedioPagoRequest request){
        return MedioPago.builder()
                    .medioPago(request.getMedioPago())
                    .importePago(request.getImportePago())
                    .build();
    }

    private ReciboResponse mapearAResponse(Recibo recibo){
        return ReciboResponse.builder()
        .numeroRecibo(recibo.getNumeroRecibo())
        .fechaRecibo(recibo.getFechaRecibo())
        .cliente(recibo.getCliente())
        .direccionCliente(recibo.getDireccionCliente())
        .ivaCliente(recibo.getIvaCliente())
        .cuitCliente(recibo.getCuitCliente())
        .localidadCliente(recibo.getLocalidadCliente())
        .numContrato(recibo.getNumContrato())
        .inicioContrato(recibo.getInicioContrato())
        .finContrato(recibo.getFinContrato())
        .propietario(recibo.getPropietario())
        .callePropiedad(recibo.getCallePropiedad())
        .localidadPropiedad(recibo.getLocalidadPropiedad())
        .cuitPropietario(recibo.getCuitPropietario())
        .conceptos(mapearConceptosAResponse(recibo.getConceptos()))
        .subtotal(recibo.getSubtotal())
        .mediosPagos(mapearMediosPagosAReponse(recibo.getMediosPagos()))
        .total(recibo.getTotal())
        .pesos(recibo.getPesos())
        .build();
    }

    private List<ConceptoResponse> mapearConceptosAResponse(List<Concepto> conceptos){
        return conceptos.stream()
        .map(c -> ConceptoResponse.builder()
        .concepto(c.getConcepto())
        .periodo(c.getPeriodo())
        .anio(c.getAnio())
        .importe(c.getImporte())
        .build())
        .collect(Collectors.toList());
    }

    private List<MedioPagoResponse> mapearMediosPagosAReponse(List<MedioPago> mediosPagos){
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

}
