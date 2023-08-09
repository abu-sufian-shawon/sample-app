package com.frutas.api;

import com.frutas.contact_us.data_model.ContactHolder;
import com.frutas.create_requisition.datamodel.ProductHolder;
import com.frutas.create_requisition.datamodel.ShopHolder;
import com.frutas.create_requisition.datamodel.UnitList;
import com.frutas.create_requisition.post_data_model.PostOrder;
import com.frutas.create_requisition.post_data_model.PostOrderResponse;
import com.frutas.previous_order.data_model.AllOrder;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiService {

    //*************** [START] Create Requisition Section **************\\
    @GET("products")
    Call<ProductHolder> getProducts();

    @GET("units")
    Call<UnitList> getUnit();

    @GET("shop/{id}")
    Call<ShopHolder> getShopData(@Path("id") String id);
    //*************** [END] **************\\


    //****************** [START] Track Order Section ********************\\
    @GET("all-order/{shopID}")
    Call<AllOrder> getAllOrder(@Path("shopID") String shopID);
    //******************* [END] ********************************\\

    //****************** [START] Contact Us Section ********************\\
    @GET("contact")
    Call<ContactHolder> getContactData();
    //******************* [END] ********************************\\


    // ************** [START] POST API *****************\\

    @POST("orders")
    @Headers("Content-Type: application/json")
    Call<PostOrderResponse> sendRequisitionOrder(@Body PostOrder postOrder);
    //************** [END] *********************\\

    // ************** [START] UPDATE API *****************\\
    @PUT("order-update/{id}")
    @Headers("Content-Type: application/json")
    Call<PostOrderResponse> updateRequisition(@Path("id") String orderId, @Body PostOrder postOrder);
    //************** [END] *********************\\


}
