package th.co.cdgs.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import th.co.cdgs.bean.CustomerDto;
import th.co.cdgs.bean.ProductDto;

@Path("product")
public class ProductController {
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getProduct() {
		List<ProductDto>  list = new ArrayList<>();
		ResultSet rs = null;
		PreparedStatement pst = null; //query
		Connection conn = null;  //DB

		try {
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/workshop", "root", "p@ssw0rd");
			pst = conn.prepareStatement( "SELECT product_id,product_name,product_desc,"
										+ "price FROM workshop.product  where active = 'Y' ");
			rs = pst.executeQuery();
			ProductDto productDto = null;
			while (rs.next()) {
				productDto = new ProductDto();
				productDto.setProductId(rs.getLong("product_Id"));
				productDto.setProductName(rs.getString("product_name"));
				productDto.setProductDesc(rs.getString("product_desc"));
				productDto.setPrice(rs.getBigDecimal("price"));
				productDto.setActive(rs.getString('Y'));
				list.add(productDto);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
			}
			try {
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (pst != null) {
			try {
				pst.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return Response.ok().entity(list).build();
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createProduct (ProductDto product) {
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/workshop", "root", "p@ssw0rd");
			pst = conn.prepareStatement(
					"INSERT INTO product (product_name,product_desc,price,'Y')" + "VALUES (?,?,?,?,?)");
			int index = 1;
			pst.setString(index++, product.getProductName());
			pst.setString(index++, product.getProductDesc());
			pst.setBigDecimal(index++, product.getPrice());
			pst.setString(index++, product.getActive());
			
			pst.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {

				try {
					rs.close();
				} catch (SQLException e) {

				}
			}
			if (pst != null) {
				try {
					pst.close();
				} catch (SQLException e) {

				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {

				}
			}
		}
		return Response.status(Status.CREATED).entity(product).build();
	}

	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateProduct(ProductDto product) {
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/workshop", "root", "p@ssw0rd");
			pst = conn.prepareStatement("UPDATE product  SET " + "product_name  = ? ," + "product_Desc = ? ,"
					+ "price = ? ," + "active = ?  ," + "WHERE product_Id = ?");
			int index = 1;
			pst.setString(index++, product.getProductName());
			pst.setString(index++, product.getProductDesc());
			pst.setBigDecimal(index++, product.getPrice());
			pst.setString(index++, product.getActive());
			pst.setLong(index++, product.getProductId());
			pst.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {

				}
			}
			if (pst != null) {
				try {
					pst.close();
				} catch (SQLException e) {

				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {

				}
			}
		}
		return Response.status(Status.CREATED).entity(product).build();
	}
	
	@DELETE
	@Path("{id}")
	public Response deleteProduct(@PathParam("id") Long productId ) {
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/workshop", "root", "p@ssw0rd");
			pst = conn.prepareStatement(
					"DELETE FROM product WHERE product_Id = ?");
			int index = 1;
			pst.setLong(index++, productId);
			pst.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {

				try {
					rs.close();
				} catch (SQLException e) {

				}
			}
			if (pst != null) {
				try {
					pst.close();
				} catch (SQLException e) {

				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {

				}
			}
		}
		return Response.status(Status.OK).build();
	}
	
}
