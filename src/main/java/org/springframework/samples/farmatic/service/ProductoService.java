
package org.springframework.samples.farmatic.service;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.farmatic.model.Farmaceutico;
import org.springframework.samples.farmatic.model.LineaPedido;
import org.springframework.samples.farmatic.model.Pedido;
import org.springframework.samples.farmatic.model.Producto;
import org.springframework.samples.farmatic.model.Proveedor;
import org.springframework.samples.farmatic.model.TipoProducto;
import org.springframework.samples.farmatic.repository.FarmaceuticoRepository;
import org.springframework.samples.farmatic.repository.ProductoRepository;
import org.springframework.samples.farmatic.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class ProductoService {

	private ProductoRepository productoRepository;

	private FarmaceuticoRepository farmaceuticoRepository;
	private UserRepository			userRepository;
	@Autowired
	public ProductoService(final ProductoRepository productoRepository, final FarmaceuticoRepository farmaceuticoRepository,final UserRepository userRepository) {
		this.productoRepository = productoRepository;
		this.farmaceuticoRepository = farmaceuticoRepository;
		this.userRepository = userRepository;
	}
	@Transactional
	public Iterable<Producto> findProducts() throws DataAccessException {
		//lista productos

		return this.productoRepository.findAll();

	}

	@Transactional
	public Producto findProductoById(final int id) throws DataAccessException {
		//detalles productos
		return this.productoRepository.findById(id);
	}

	

	@Transactional
	public void saveProducto(final Producto product) throws DataAccessException {
		//creating product
		this.productoRepository.save(product);
	}
	
	@Transactional
	public Producto findProductoByCode(final String code) throws DataAccessException {
		//detalles productos (code)
		return this.productoRepository.findByCode(code);
	}
	
	public Collection<TipoProducto> getProductTypes() throws DataAccessException {
		Collection<TipoProducto> productTypes = new ArrayList<TipoProducto>();
		productTypes.add(TipoProducto.ESTUPEFACIENTE);
		productTypes.add(TipoProducto.FARMACOCONRECETA);
		productTypes.add(TipoProducto.FARMACOSINRECETA);
		productTypes.add(TipoProducto.PARAFARMACIA);
		
		return productTypes;
		
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
