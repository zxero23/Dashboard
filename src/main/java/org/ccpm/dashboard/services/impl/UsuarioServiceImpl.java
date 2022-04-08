package org.ccpm.dashboard.services.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.ccpm.dashboard.dtos.PersonaDto;
import org.ccpm.dashboard.dtos.UsuarioDto;
import org.ccpm.dashboard.entities.Municipio;
import org.ccpm.dashboard.entities.Persona;
import org.ccpm.dashboard.entities.Usuario;
import org.ccpm.dashboard.exceptions.RecordNotFoundException;
import org.ccpm.dashboard.repositories.MunicipioRepository;
import org.ccpm.dashboard.repositories.PersonaRepository;
import org.ccpm.dashboard.repositories.RolRepository;
import org.ccpm.dashboard.repositories.UsuarioRepository;
import org.ccpm.dashboard.services.UsuarioService;
import org.ccpm.dashboard.utilities.EncriptadorAES;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class UsuarioServiceImpl implements UsuarioService {

	@Value("${secretKey}")
	private String secretKey;

	@Autowired
	UsuarioRepository usuarioRepository;
	@Autowired
	PersonaRepository personaRepository;
	@Autowired
	RolRepository rolRepository;
	@Autowired
	MunicipioRepository municipioRepository;

	@Override
	public List<UsuarioDto> findAll(Specification<Usuario> spec, Pageable pageable) {
		return usuarioRepository.findAll(spec, pageable).getContent().stream().map(this::convertToDto)
				.collect(Collectors.toList());
	}

	@Override
	public UsuarioDto login(String nombreUsuario, String claveAcceso) throws Exception {
		EncriptadorAES encriptadorAES = new EncriptadorAES();
		Usuario usuario = this.usuarioRepository
				.findByNombreUsuarioAndClaveAcceso(nombreUsuario, encriptadorAES.encriptar(claveAcceso, secretKey))
				.orElse(null);
		if (usuario != null) {
			return this.convertToDto(usuario);
		} else {
			throw new RecordNotFoundException("Usuario no encontrado.");
		}
	}

	private UsuarioDto convertToDto(Usuario Usuario) {
		ModelMapper modelMapper = new ModelMapper();
		return modelMapper.map(Usuario, UsuarioDto.class);
	}

	private Usuario convertToEntity(UsuarioDto UsuarioDto) {
		ModelMapper modelMapper = new ModelMapper();
		return modelMapper.map(UsuarioDto, Usuario.class);
	}
	/*
	private PersonaDto convertPersonaToDto(Persona persona) {
		ModelMapper modelMapper = new ModelMapper();
		return modelMapper.map(persona, PersonaDto.class);
	}
    */
	private Persona convertPersonaDtoToEntity(PersonaDto personaDto) {
		ModelMapper modelMapper = new ModelMapper();
		return modelMapper.map(personaDto, Persona.class);
	}

	@Override
	public UsuarioDto find(Long id) {
		Usuario usuario = this.usuarioRepository.getById(id);
		if (usuario != null) {
			return this.convertToDto(usuario);
		} else {
			throw new RecordNotFoundException("No se encontró usuario con id: " + id);
		}
	}

	@Override
	public UsuarioDto create(UsuarioDto usuarioDto) throws Exception {
		usuarioDto.setEstado(true);
		Persona persona = this.personaRepository.saveAndFlush(this.convertPersonaDtoToEntity(usuarioDto.getPersona()));
		EncriptadorAES encriptadorAES = new EncriptadorAES();
		Usuario usuario = this.convertToEntity(usuarioDto);
		usuario.setPersona(persona);
		usuario.setClaveAcceso(encriptadorAES.encriptar(usuarioDto.getClaveAcceso(), secretKey));
		return this.convertToDto(this.usuarioRepository.saveAndFlush(usuario));
	}

	@Override
	public UsuarioDto edit(UsuarioDto usuarioDto) throws Exception {
		Usuario usuario = this.usuarioRepository.getById(usuarioDto.getId());
		Municipio municipio = municipioRepository.getById(usuarioDto.getPersona().getMunicipio().getId());
		EncriptadorAES encriptadorAES = new EncriptadorAES();
		if (usuario != null) {
			usuario.setNombreUsuario(usuarioDto.getNombreUsuario());
			usuario.setRol(this.rolRepository.findById(usuarioDto.getRol().getId()).orElse(null));
			usuario.setClaveAcceso(encriptadorAES.encriptar(usuarioDto.getClaveAcceso(), secretKey));
			/*Datos de la persona*/
			usuario.getPersona().setApellidoNombreRazonSocial(usuarioDto.getPersona().getApellidoNombreRazonSocial());
			usuario.getPersona().setDocumento(usuarioDto.getPersona().getDocumento());
			usuario.getPersona().setDomicilio(usuarioDto.getPersona().getDomicilio());
			usuario.getPersona().setFechaNacimiento(usuarioDto.getPersona().getFechaNacimiento());
			usuario.getPersona().setMunicipio(municipio);
			usuario.getPersona().setNacionalidad(usuarioDto.getPersona().getNacionalidad());
			usuario.getPersona().setTelefono(usuarioDto.getPersona().getTelefono());
			usuario.getPersona().setCorreo(usuarioDto.getPersona().getCorreo());
			this.personaRepository.save(usuario.getPersona());
			return this.convertToDto(this.usuarioRepository.saveAndFlush(usuario));
		} else {
			throw new RecordNotFoundException("No se encontró usuario con id: " + usuarioDto.getId());
		}
	}

	@Override
	public UsuarioDto remove(Long id) throws Exception {
		Usuario usuario = this.usuarioRepository.getById(id);
		if (usuario != null) {
			usuario.setEstado(false);
			return this.convertToDto(this.usuarioRepository.saveAndFlush(usuario));
		} else {
			throw new RecordNotFoundException("No se encontró usuario con id: " + id);
		}
	}

	@Override
	public Long count(Specification<Usuario> spec) {
		return usuarioRepository.count(spec);
	}

	@Override
	public UsuarioDto findByUsuario(String usuario) {
		try {
			Usuario usuarioAux = usuarioRepository.findByNombreUsuario(usuario).orElse(null);
			if(usuarioAux!=null) {
				return this.convertToDto(usuarioAux);
			}else {
				return null;
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}
	

}
