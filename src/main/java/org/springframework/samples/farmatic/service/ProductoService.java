
package org.springframework.samples.farmatic.service;

import java.util.Collection;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.farmatic.model.Farmaceutico;
import org.springframework.samples.farmatic.model.Producto;
import org.springframework.samples.farmatic.model.TipoMedicamento;
import org.springframework.samples.farmatic.repository.FarmaceuticoRepository;
import org.springframework.samples.farmatic.repository.ProductoRepository;
import org.springframework.samples.farmatic.repository.TipoMedicamentoRepository;
import org.springframework.samples.farmatic.repository.UserRepository;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ProductoService {

	private ProductoRepository productoRepository;
	
	private TipoMedicamentoRepository tipoMedicamentoRepository;

	private FarmaceuticoRepository farmaceuticoRepository;
	
	private UserRepository userRepository;
	
	@Autowired
	public ProductoService(final ProductoRepository productoRepository, final TipoMedicamentoRepository tipoMedicamentoRepository, final FarmaceuticoRepository farmaceuticoRepository,final UserRepository userRepository) {
		this.productoRepository = productoRepository;
		this.farmaceuticoRepository = farmaceuticoRepository;
		this.userRepository = userRepository;
		this.tipoMedicamentoRepository = tipoMedicamentoRepository;
	}
	
	@Transactional
	public Iterable<Producto> findProducts() throws DataAccessException {
		//lista productos

		return this.productoRepository.findAll();

	}
	
	@Transactional
	public Collection<Producto> findProductosByTipo(TipoMedicamento tipo) throws DataAccessException{
		return this.productoRepository.findByTipoMedicamento(tipo);
	}

	@Transactional
	public Producto findProductoById(final int id) throws DataAccessException {
		//detalles productos
		return this.productoRepository.findById(id);
	}

	@Transactional
	public void saveProducto(final Producto producto) throws DataAccessException {
		//creating product
		producto.setName(producto.getName().toUpperCase());
		this.productoRepository.save(producto);
	}
	
	@Transactional
	public Producto findProductoByCode(final String code) throws DataAccessException {
		//detalles productos (code)
		Producto producto = this.productoRepository.findByCode(code);
		if(producto == null) {
			producto = new Producto();
			log.warn("El producto con el codigo '" + code + "' no existe en la BD");
		}
		return producto;
	}
	
	@Transactional
	public Collection<Producto> productoPorNombre(String name) throws DataAccessException {
		return this.productoRepository.findAllByName(name);
	}
	
	@Transactional
	public Collection<TipoMedicamento> getMedicamentoTypes() throws DataAccessException {
		Collection<TipoMedicamento> tipos = this.tipoMedicamentoRepository.findAll();
		
		return tipos;
	}

	//------Metodo referente a Farmaceutico
	@Transactional
	public Farmaceutico farmaceutico(final int id) {
		return this.farmaceuticoRepository.findById(id);
	}
	@Transactional(readOnly = true)
	public Collection<Farmaceutico> findFarmaceuticos() {
		return (Collection<Farmaceutico>) this.farmaceuticoRepository.findAll();
	}
}
