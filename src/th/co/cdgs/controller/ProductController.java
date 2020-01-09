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
	
}
