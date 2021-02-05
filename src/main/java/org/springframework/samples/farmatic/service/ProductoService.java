
package org.springframework.samples.farmatic.service;

import java.util.Collection;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.farmatic.model.Producto;
import org.springframework.samples.farmatic.model.TipoMedicamento;
import org.springframework.samples.farmatic.repository.ProductoRepository;
import org.springframework.samples.farmatic.repository.TipoMedicamentoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ProductoService {

	private ProductoRepository			productoRepository;

	private TipoMedicamentoRepository	tipoMedicamentoRepository;


	@Autowired
	public ProductoService(final ProductoRepository productoRepository, final TipoMedicamentoRepository tipoMedicamentoRepository) {
		this.productoRepository = productoRepository;
		this.tipoMedicamentoRepository = tipoMedicamentoRepository;
	}

	@Transactional
	public Iterable<Producto> findProducts() throws DataAccessException {
		//busqueda de todos los productos
		return this.productoRepository.findAll();

	}

	@Transactional
	public Collection<Producto> findProductosByTipo(final TipoMedicamento tipo) throws DataAccessException {
		//busqueda de productos por tipo de medicamento
		return this.productoRepository.findByTipoMedicamento(tipo);
	}

	@Transactional
	public Producto findProductoById(final int id) throws DataAccessException {
		//busqueda de un producto por el id
		return this.productoRepository.findById(id);
	}

	@Transactional(rollbackFor = EntityNotFoundException.class)
	public Producto findProductoByCode(final String code) throws DataAccessException, EntityNotFoundException {
		//busqueda de un producto por su codigo
		Producto producto = this.productoRepository.findByCode(code);
		if (producto == null) {//si no existe lanza una excepcion
			ProductoService.log.warn("El producto con el codigo '" + code + "' no existe en la BD");
			throw new EntityNotFoundException();
		}
		return producto;
	}

	@Transactional(rollbackFor = EntityNotFoundException.class)
	public Collection<Producto> productoPorNombre(final String name) throws DataAccessException, EntityNotFoundException {
		//busqueda de productos por coincidencias en su nombre
		Collection<Producto> productos = this.productoRepository.findAllByName(name);
		if (productos.isEmpty()) {
			ProductoService.log.warn("El producto con la busqueda '" + name + "' no existe en la BD");
			throw new EntityNotFoundException();
		}
		return productos;
	}

	@Transactional
	public void saveProducto(final Producto producto) throws DataAccessException {
		//crear o modificar un producto
		producto.setName(producto.getName().toUpperCase());//se guarda el nombre en mayusculas
		ProductoService.log.debug("El producto '" + producto.getCode() + "' - " + producto.getName() + ", " + producto.getPvp() + "€ se ha creado o modificado");
		this.productoRepository.save(producto);
	}

	@Transactional
	public Collection<TipoMedicamento> getMedicamentoTypes() throws DataAccessException {
		//busqueda de todos los tipo de medicamentos
		return this.tipoMedicamentoRepository.findAll();
	}

}
