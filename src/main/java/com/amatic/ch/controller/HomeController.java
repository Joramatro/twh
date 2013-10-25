package com.amatic.ch.controller;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.amatic.ch.constants.WebConstants;
import com.amatic.ch.dto.Comentario;
import com.amatic.ch.dto.Publicacion;
import com.amatic.ch.exception.UnknownResourceException;
import com.amatic.ch.service.ComentarioService;
import com.amatic.ch.service.PublicacionService;
import com.amatic.ch.utils.Mail;
import com.amatic.ch.utils.WebUtils;

@Controller
public class HomeController {

    List<Integer> sessions = new ArrayList<Integer>();

    @Autowired
    private PublicacionService publicacionService;

    @Autowired
    private ComentarioService comentarioService;

    @RequestMapping(value = { "/index", "/" }, method = { RequestMethod.GET,
	    RequestMethod.POST })
    public String getMainScreen(ModelMap model, HttpServletRequest request,
	    HttpServletResponse response) throws IOException {

	HttpSession session = request.getSession();
	response.setDateHeader("Expires", (new Date()).getTime() + 604800000L);
	// User user = (User) session
	// .getAttribute(WebConstants.SessionConstants.RC_USER);
	// // Ref<?> value has not been initialized
	// if (user != null) {
	// user = this.userService.findUser(user.getMail());
	// }
	// // Saltando Uservalidation
	// if (user == null) {
	// user = new User();
	// user.setMail((String) oIdUserBean.getAttribute("email"));
	// user.setName((String) oIdUserBean.getAttribute("nickname"));
	// session.setAttribute(WebConstants.SessionConstants.RC_USER, user);
	// try {
	// user = this.userService.findUser(user.getMail());
	// } catch (com.googlecode.objectify.NotFoundException nf) {
	// this.userService.create(user, false);
	// }
	// }
	// user.setNewUser(true);
	// Fin Uservalidation trick

	List<Publicacion> publicacionesEbooks = publicacionService
		.getUltimasPublicaciones(WebConstants.SessionConstants.EBOOK);

	List<Publicacion> publicacionesBlog = publicacionService
		.getUltimasPublicaciones(WebConstants.SessionConstants.ARTICULO);

	List<Publicacion> publicacionesMVE = publicacionService
		.getPublicacionesMasVistas(WebConstants.SessionConstants.EBOOK);

	List<Publicacion> publicacionesMVA = publicacionService
		.getPublicacionesMasVistas(WebConstants.SessionConstants.ARTICULO);

	List<Publicacion> publicacionesDestacadas = publicacionService
		.getPublicacionesDestacadas();

	List<Publicacion> publicacionesPortada = publicacionService
		.getPublicacionesPortada();

	List<Comentario> comentarios = comentarioService
		.getUltimosComentarios();

	model.addAttribute("comentarios", comentarios);

	model.addAttribute("publicacionesMVE", publicacionesMVE);

	model.addAttribute("publicacionesMVA", publicacionesMVA);

	model.addAttribute("publicacionesEbooks", publicacionesEbooks);

	model.addAttribute("publicacionesBlog", publicacionesBlog);

	model.addAttribute("publicacionesDestacadas", publicacionesDestacadas);

	model.addAttribute("publicacionesPortada", publicacionesPortada);

	return "index";
    }

    @RequestMapping(value = { "/venta/{tipo}/{url}" }, method = { RequestMethod.GET })
    public String getVenta(ModelMap model, @PathVariable String url,
	    @PathVariable String tipo, HttpServletRequest request,
	    HttpServletResponse response) throws IOException,
	    NoSuchAlgorithmException {
	HttpSession session = request.getSession();

	String key = WebUtils.SHA1(url.replaceAll("-", " "));
	Publicacion publicacion = null;
	if (tipo.equals("principal")) {
	    publicacion = publicacionService.getPublicacion(key,
		    WebConstants.SessionConstants.EBOOK);
	    if (publicacion == null) {
		publicacion = publicacionService.getPublicacion(key,
			WebConstants.SessionConstants.ARTICULO);
	    }
	} else if (tipo.equals("extra")) {
	    publicacion = publicacionService.getPublicacion(key,
		    WebConstants.SessionConstants.ACCESORIO);
	} else if (tipo.equals("marca")) {
	    publicacion = new Publicacion();
	    if (url.equals("logo1")) {
		publicacion.setScript(WebConstants.SessionConstants.logo1);
		publicacion.setScript2(WebConstants.SessionConstants.logo1img);
	    } else if (url.equals("logo2")) {
		publicacion.setScript(WebConstants.SessionConstants.logo2);
		publicacion.setScript2(WebConstants.SessionConstants.logo2img);
	    } else if (url.equals("logo3")) {
		publicacion.setScript(WebConstants.SessionConstants.logo3);
		publicacion.setScript2(WebConstants.SessionConstants.logo3img);
	    } else if (url.equals("logo4")) {
		publicacion.setScript(WebConstants.SessionConstants.logo4);
		publicacion.setScript2(WebConstants.SessionConstants.logo4img);
	    } else if (url.equals("logo5")) {
		publicacion.setScript(WebConstants.SessionConstants.logo5);
		publicacion.setScript2(WebConstants.SessionConstants.logo5img);
	    } else if (url.equals("logo6")) {
		publicacion.setScript(WebConstants.SessionConstants.logo6);
		publicacion.setScript2(WebConstants.SessionConstants.logo6img);
	    } else if (url.equals("logo7")) {
		publicacion.setScript(WebConstants.SessionConstants.logo7);
		publicacion.setScript2(WebConstants.SessionConstants.logo7img);
	    } else if (url.equals("logo8")) {
		publicacion.setScript(WebConstants.SessionConstants.logo8);
		publicacion.setScript2(WebConstants.SessionConstants.logo8img);
	    } else if (url.equals("logo9")) {
		publicacion.setScript(WebConstants.SessionConstants.logo9);
		publicacion.setScript2(WebConstants.SessionConstants.logo9img);
	    } else if (url.equals("logo10")) {
		publicacion.setScript(WebConstants.SessionConstants.logo10);
		publicacion.setScript2(WebConstants.SessionConstants.logo10img);
	    } else {
		publicacion = null;
	    }
	}

	if (publicacion == null) {
	    String uri = request.getRequestURI();
	    throw new UnknownResourceException("There is no resource for path "
		    + uri);
	    // return "channelNotFound";
	}

	StringBuffer mensaje = new StringBuffer();
	Enumeration<String> headerNames = request.getHeaderNames();
	while (headerNames.hasMoreElements()) {
	    String headerName = headerNames.nextElement();
	    mensaje.append(headerName);
	    String headerValue = request.getHeader(headerName);
	    mensaje.append(", " + headerValue);
	    mensaje.append("\n");
	}
	Mail.sendMail(mensaje.toString(), "CCH " + request.getRequestURI());

	model.addAttribute("publicacion", publicacion);

	return "venta/venta";
    }

}
