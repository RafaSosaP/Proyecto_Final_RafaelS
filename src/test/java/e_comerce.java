import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import com.jayway.jsonpath.JsonPath;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import static io.restassured.RestAssured.*;
import static org.junit.Assert.*;
@FixMethodOrder(MethodSorters.NAME_ASCENDING)

public class e_comerce {

    //variables
    static private String base_url = "webapi.segundamano.mx";
    static private String token; // este es el token que tiene v2 al final
    static private String account_id; //= /private/accounts/5240470
    static private String uuid; // este es su valor d790424d-2953-4fbd-86bf-e47a39915273
    static private String idAdddress; //este es el valor del ID Address
    static private String AlertID; //este es el valor del ID de la alerta
    static private String AdID; //este es el ID del anuncio, el cual se creará después de haberse borrado el anuncio anterior

    //Función para crear un token fresco
    private String getToken(){

        RestAssured.baseURI = String.format("https://%s/nga/api/v1.1/private/accounts",base_url);

        Response resp = given()
                .log().all()
                .queryParam("lang","es")
                .auth().preemptive().basic("rafael.sosap@gmail.com", "Sopral0408!") //libreria rest assured
                //.header("Authorization","Basic " + token_basic)
                .post();

        String body_response = resp.getBody().asString();
        System.out.println("Body response: " + body_response);

        //Definimos las variables nuevas con base a lo que obtuvimos en respuesta
        token = JsonPath.read(body_response,"$.access_token");
        System.out.println("token: " + token);

        uuid = JsonPath.read(body_response,"$.account.uuid");
        System.out.println("uuid: " + uuid);

        String datos = uuid + ":" + token; //definimos datos (lo que está dentro del base 64 del basic {{}}

        String encodedAuth = Base64.getEncoder().encodeToString(datos.getBytes());

        return encodedAuth; //esto es lo que determina que el resultado de la función lo regresa en encodedAuth
    }


    @Test
    public void Rafa_t00_obtener_token(){
        //Configurar URI
        RestAssured.baseURI = String.format("https://%s/nga/api/v1.1/private/accounts",base_url);
        String token_basic = "dmVudGFzNDcyODM3Njg3QG1haWxpbmF0b3IuY29tOjEyMzQ1";

        Response resp = given()
                .log().all()
                .queryParam("lang","es")
                .header("Authorization","Basic " + token_basic)
                .post();

        String body_response = resp.getBody().asString();
        String headers_response = resp.getHeaders().toString();
        //String body_p = response.prettyPrint();
        System.out.println("Body response: " + body_response);
        System.out.println("Headers response: " + headers_response);
        //System.out.println("Body response: " + body_p);

        assertEquals(200, resp.getStatusCode());
        assertNotNull(body_response);
        assertTrue(body_response.contains("access_token"));
    }

@Test
    public void Rafa_t01_crear_usuario(){
        //Configurar URI
    RestAssured.baseURI = String.format("https://%s/nga/api/v1.1/private/accounts", base_url);


    //System.out.println("usuario y contraseña son: " + emailnator  +  passmailinator);
    String bodybody = "{\"account\":{\"email\":\"ventas51120@mailinator.com\n\"}}";


    //declaramos un objeto, el cual viene por parte de Rest Assured. También declaramos la variable response sin mayus por ser palabra reservada
    Response response = given()
            .log()//con esto muestra la respuesta
            .all() // con esto muestra lo que está involucrado dentro de la llamada
            .queryParam("lang", "es")

            //HEADERS:
            .auth().preemptive().basic("ventas51120@mailinator.com\n", "51120") //libreria rest assured que reemplaza el Basic {{ }}
            .header("Content-Type","application/json")
            .header("Origin","https://www.segundamano.mx")

            //BODY:
            .body(bodybody)

            .post(); //se pone el método

    // variables para convertir la respuesta en string
    String body_response = response.getBody().asString();
    String headers_response = response.getHeaders().toString();

    //impresiones que hace el sistema
    System.out.println("Body response:" + response.getStatusCode()); //con esto imprimimos la respuesta y el status code
    System.out.println("Body response: " + body_response);
    System.out.println("Headers response: " + headers_response);

    //PRUEBAS
    assertEquals(401,response.getStatusCode());
    assertNotNull(body_response);
    assertTrue(body_response.contains("ACCOUNT_VERIFICATION_REQUIRED"));
    }


    @Test
    public void Rafa_t02_obtener_token_con_Basic_Auth_email_pass(){
        //Configurar URI
        RestAssured.baseURI = String.format("https://%s/nga/api/v1.1/private/accounts",base_url);
        //String token_basic = "dmVudGFzNDcyODM3Njg3QG1haWxpbmF0b3IuY29tOjEyMzQ1";

        Response resp = given()
                .log().all()
                .queryParam("lang","es")
                .auth().preemptive().basic("rafael.sosap@gmail.com", "Sopral0408!") //libreria rest assured
                //.header("Authorization","Basic " + token_basic)
                .post();

        String body_response = resp.getBody().asString();
        String headers_response = resp.getHeaders().toString();
        //String body_p = response.prettyPrint();
        System.out.println("Body response: " + body_response);


        assertEquals(200, resp.getStatusCode());
        assertNotNull(body_response);
        assertTrue(body_response.contains("access_token"));

        //Definimos las variables nuevas con base a lo que obtuvimos en respuesta
        token = JsonPath.read(body_response,"$.access_token");
        account_id = JsonPath.read(body_response,"$.account.account_id");
        uuid = JsonPath.read(body_response,"$.account.uuid");

        System.out.println("token: " + token);
        System.out.println("account_id: " + JsonPath.read(body_response,"$.account.account_id"));
        System.out.println("uuid: " + JsonPath.read(body_response,"$.account.uuid"));



        //console.log("Token: ", responseJson.access_token)
        //console.log("account_id: ",responseJson.account.account_id)
        //console.log("uuid: ", responseJson.account.uuid)
    }


    @Test
    public void Rafa_t03_editar_datos_usuario(){
        //Configurar URI

        RestAssured.baseURI = String.format("https://%s/nga/api/v1/%s",base_url,account_id);

        String body2 = "{\"account\":{\"name\":\"Rafael Alejandro Sosa\",\"phone\":\"3312670544\",\"professional\":false}}";

        Response resp = given()
                .log().all()
                .header("Authorization","tag:scmcoord.com,2013:api " + token)
                .header("Content-Type","application/json")
                .header("Origin","https://www.segundamano.mx")
                .body(body2)
                .patch();

        String body_response = resp.getBody().asString();
        String headers_response = resp.getHeaders().toString();
        //String body_p = response.prettyPrint();
        System.out.println("Body response: " + body_response);
        System.out.println("Headers response: " + headers_response);
        //System.out.println("Body response: " + body_p);

        assertEquals(200, resp.getStatusCode());
        assertNotNull(body_response);
        assertTrue(body_response.contains("account"));
    }


    @Test
    public void Rafa_t04_crear_favoritos(){
        //https://{{url_base}}/favorites/v1/private/accounts/{{uuid}}

        RestAssured.baseURI = String.format("https://webapi.segundamano.mx/favorites/v1/private/accounts/%s",uuid);


        //System.out.println("usuario y contraseña son: " + emailnator  +  passmailinator);
        String body = "{\"list_ids\":[937412221]}";


        //declaramos un objeto, el cual viene por parte de Rest Assured. También declaramos la variable response sin mayus por ser palabra reservada
        Response response = given()
                .log()//con esto muestra la respuesta
                .all() // con esto muestra lo que está involucrado dentro de la llamada
                //.queryParam("lang", "es")

                //HEADERS:
                .auth().preemptive().basic(uuid,token) //libreria rest assured que reemplaza el Basic {{ }}
                .header("Content-Type","application/json;charset=UTF-8")

                //BODY:
                .body(body)

                .post(); //se pone el método

        // variables para convertir la respuesta en string
        String body_response = response.getBody().asString();
        String headers_response = response.getHeaders().toString();

        //impresiones que hace el sistema
        System.out.println("Body response:" + response.getStatusCode()); //con esto imprimimos la respuesta y el status code
        System.out.println("Body response: " + body_response);
        System.out.println("Headers response: " + headers_response);

        //PRUEBAS
        assertEquals(200,response.getStatusCode());
        assertNotNull(body_response);
        assertTrue(body_response.contains("added"));
    }

    /*
    @Test
    public void Rafa_t05_borrar_anuncio(){
        // Url de esta petición: https://{{url_base}}/nga/api/v1{{account_id}}/klfst/{{ad_id}}

        RestAssured.baseURI = String.format("https://%s/nga/api/v1%s/klfst/%s",base_url,account_id,AdID);
        String body ="{\"delete_reason\":{\"code\":\"0\"}}";

        Response response = given()
                .log()
                .all()

                //HEADERS
                .header("Authorization","tag:scmcoord.com,2013:api " + token)
                .header("Origin","https://www.segundamano.mx")
                .header("Content-Type","application/json")

                //BODY:
                .body(body)

                //METHOD:
                .delete();

        // Definimos respuesta en string y headers
        String body_response = response.getBody().asString();
        String headers_response = response.getHeaders().toString();

        //impresiones que hace el sistema
        System.out.println("Body response:" + response.getStatusCode()); //con esto imprimimos la respuesta y el status code
        System.out.println("Body response: " + body_response);
        System.out.println("Headers response: " + headers_response);

        //PRUEBAS
        assertEquals(200,response.getStatusCode());
        assertNotNull(body_response);


    }
*/

    @Test
    public void Rafa_t06_crear_anuncio() {
        //tenemos que crear un token fresco

        String newToken = getToken();

        System.out.println(("Test que regresa la función: " + newToken));

        //URL para este request: https://{{url_base}}/v2/accounts/{{uuid}}/up
        RestAssured.baseURI = String.format("https://%s/v2/accounts/%s/up", base_url, uuid);

        String body = "{\"category\":\"8086\",\"subject\":\"Anuncio a Borrarse últimoo!!\",\"body\":\"este anuncio se borrará pa la prueba final\",\"price\":\"200\",\"region\":\"15\",\"municipality\":\"469\",\"area\":\"46371\",\"phone_hidden\":\"true\",\"show_phone\":\"false\",\"contact_phone\":\"3322555542\"}";

        Response response = given()
                .log()
                .all()

                //HEADERS
                .header("Authorization", "Basic " + newToken)
                .header("Origin","https://www.segundamano.mx")
                .header("Content-Type","application/json")
                .header("Accept", "application/json, text/plain, */*")
                .header("x-source", "PHOENIX_DESKTOP")

                .body(body)
                .post();

        // Definimos respuesta en string y headers
        String body_response = response.getBody().asString();
        String headers_response = response.getHeaders().toString();

        //impresiones que hace el sistema
        System.out.println("Body response:" + response.getStatusCode()); //con esto imprimimos la respuesta y el status code
        System.out.println("Body response: " + body_response);
        System.out.println("Headers response: " + headers_response);

        //PRUEBAS
        assertEquals(200,response.getStatusCode());
        assertNotNull(body_response);
        assertTrue(body_response.contains("ad_id"));


        AdID = JsonPath.read(body_response,"$.data.ad.ad_id");
        System.out.println("Address ID:" + AdID); //con esto imprimimos la respuesta y el status code

    }


    @Test
    public void Rafa_t07_crear_alerta(){
// Url de esta petición: https://{{url_base}}/alerts/v1/private/account/{{uuid}}/alert

        RestAssured.baseURI = String.format("https://webapi.segundamano.mx/alerts/v1/private/account/%s/alert",uuid);
        //BODY:
        String body = "{\"ad_listing_service_filters\":{\"region\":\"16\",\"municipality\":\"589\",\"area\":\"47682\",\"category_lv0\":\"1000\",\"category_lv1\":\"1040\",\"estate_type\":\"1\"}}";

        Response response = given()
                .log()
                .all()

                //HEADERS
                .auth().preemptive().basic(uuid,token)
                .header("Content-Type", "application/json")
                .header("Origin", "https://www.segundamano.mx")

                //BODY
                .body(body)

                //METHOD:
                .post();

        // Definimos respuesta en string y headers
        String body_response = response.getBody().asString();
        String headers_response = response.getHeaders().toString();

        //Creamos la variable idAdddress obtenida de la respuesta

        //account_id = JsonPath.read(body_response,"$.account.account_id");
        AlertID = JsonPath.read(body_response,"$.data.alert.id");
        System.out.println("Alert ID:" + AlertID); //con esto imprimimos la respuesta y el status code

        //impresiones que hace el sistema
        System.out.println("Body response:" + response.getStatusCode()); //con esto imprimimos la respuesta y el status code
        System.out.println("Body response: " + body_response);
        System.out.println("Headers response: " + headers_response);

        //PRUEBAS
        assertEquals(200,response.getStatusCode());
        assertNotNull(body_response);
        assertTrue(body_response.contains("title"));

    }


//CRUD - CREATE READ UPDATE Y DELETE

    //Crear Dirección

    @Test
    public void Rafa_t08_crear_direccion(){
// Url de esta petición: https://webapi.segundamano.mx/addresses/v1/create

        RestAssured.baseURI = String.format("https://%s/addresses/v1/create",base_url);

        Response response = given()
                .log()
                .all()

                //HEADERS
                .auth().preemptive().basic(uuid,token)
                .header("Content-type", "application/x-www-form-urlencoded")
                .header("Origin", "https://www.segundamano.mx")

                //BODY:
                //se define con form Param (Rest-Assured) los campos que tendrá contenidos en el body.
                .formParam("contact", "Casa Intelli J")
                .formParam("phone", "3319842905")
                .formParam("rfc", "SOPA900809")
                .formParam("zipCode", "45888")
                .formParam("exteriorInfo", "Sierra Nevada")
                .formParam("interiorInfo", "485")
                .formParam("region", "5")
                .formParam("municipality", "51")
                .formParam("area", "6537")
                .formParam("alias", "La casa del éxito!")

                //METHOD:
                .post();

        // Definimos respuesta en string y headers
        String body_response = response.getBody().asString();
        String headers_response = response.getHeaders().toString();

        //Creamos la variable idAdddress obtenida de la respuesta
        idAdddress = JsonPath.read(body_response,"$.addressID");
        System.out.println("Address ID:" + idAdddress); //con esto imprimimos la respuesta y el status code

        //impresiones que hace el sistema
        System.out.println("Body response:" + response.getStatusCode()); //con esto imprimimos la respuesta y el status code
        System.out.println("Body response: " + body_response);
        System.out.println("Headers response: " + headers_response);

        //PRUEBAS
        assertEquals(201,response.getStatusCode());
        assertNotNull(body_response);
        assertTrue(body_response.contains("addressID"));

    }


    @Test
    public void Rafa_t09_leer_direccion(){
// Url de esta petición: https://webapi.segundamano.mx/addresses/v1/create

        RestAssured.baseURI = String.format("https://%s/addresses/v1/get/",base_url);

        Response response = given()
                .log()
                .all()

                //HEADERS
                .auth().preemptive().basic(uuid,token)

                //METHOD:
                .get();

        // Definimos respuesta en string y headers
        String body_response = response.getBody().asString();
        String headers_response = response.getHeaders().toString();


        //impresiones que hace el sistema
        System.out.println("Body response:" + response.getStatusCode()); //con esto imprimimos la respuesta y el status code
        System.out.println("Body response: " + body_response);
        System.out.println("Headers response: " + headers_response);

        //PRUEBAS
        assertEquals(200,response.getStatusCode());
        assertNotNull(body_response);
        assertTrue(body_response.contains("addresses"));

    }

    @Test
    public void Rafa_t10_modificar_direccion(){
// Url de esta petición: https://{{url_base}}/addresses/v1/modify/{{idAdddress}}

        RestAssured.baseURI = String.format("https://%s/addresses/v1/modify/%s",base_url,idAdddress);

        Response response = given()
                .log()
                .all()

                //HEADERS
                .auth().preemptive().basic(uuid,token)
                .header("Content-type", "application/x-www-form-urlencoded")
                .header("Origin", "https://www.segundamano.mx")

                //BODY:
                //se define con form Param (Rest-Assured) los campos que tendrá contenidos en el body.
                .formParam("contact", "Casa Intelli J")
                .formParam("phone", "3319842905")
                .formParam("rfc", "SOPA900809")
                .formParam("zipCode", "45888")
                .formParam("exteriorInfo", "Sierra Nevada")
                .formParam("interiorInfo", "485")
                .formParam("region", "5")
                .formParam("municipality", "51")
                .formParam("area", "6537")
                .formParam("alias", "La casa del éxito modificada!")

                //METHOD:
                .put();

        // Definimos respuesta en string y headers
        String body_response = response.getBody().asString();
        String headers_response = response.getHeaders().toString();

        //impresiones que hace el sistema
        System.out.println("Body response:" + response.getStatusCode()); //con esto imprimimos la respuesta y el status code
        System.out.println("Body response: " + body_response);
        System.out.println("Headers response: " + headers_response);

        //PRUEBAS
        assertEquals(200,response.getStatusCode());
        assertNotNull(body_response);
        assertTrue(body_response.contains("modified correctly"));

    }

    @Test
    public void Rafa_t11_modificar_direccion_sin_body(){
// Url de esta petición: https://{{url_base}}/addresses/v1/modify/{{idAdddress}}

        RestAssured.baseURI = String.format("https://%s/addresses/v1/modify/%s",base_url,idAdddress);

        Response response = given()
                .log()
                .all()

                //HEADERS
                .auth().preemptive().basic(uuid,token)
                .header("Content-type", "application/x-www-form-urlencoded")
                .header("Origin", "https://www.segundamano.mx")

                //BODY:
                //como lo vimos en asesoría, no necesita de un body por que refresca unicamente la dirección, así trabaja el endpoint

                //METHOD:
                .put();

        // Definimos respuesta en string y headers
        String body_response = response.getBody().asString();
        String headers_response = response.getHeaders().toString();

        //impresiones que hace el sistema
        System.out.println("Body response:" + response.getStatusCode()); //con esto imprimimos la respuesta y el status code
        System.out.println("Body response: " + body_response);
        System.out.println("Headers response: " + headers_response);

        //PRUEBAS
        assertEquals(200,response.getStatusCode());
        assertNotNull(body_response);
        assertTrue(body_response.contains("modified correctly"));

    }

    @Test
    public void Rafa_t12_borrar_direccion(){
// Url de esta petición: https://{{url_base}}/addresses/v1/modify/{{idAdddress}}

        RestAssured.baseURI = String.format("https://%s/addresses/v1/delete/%s",base_url,idAdddress);

        Response response = given()
                .log()
                .all()

                //HEADERS
                .auth().preemptive().basic(uuid,token)
                .header("Origin", "https://www.segundamano.mx")

                //BODY:

                //METHOD:
                .delete();

        // Definimos respuesta en string y headers
        String body_response = response.getBody().asString();
        String headers_response = response.getHeaders().toString();

        //impresiones que hace el sistema
        System.out.println("Body response:" + response.getStatusCode()); //con esto imprimimos la respuesta y el status code
        System.out.println("Body response: " + body_response);
        System.out.println("Headers response: " + headers_response);

        //PRUEBAS
        assertEquals(200,response.getStatusCode());
        assertNotNull(body_response);
        assertTrue(body_response.contains(idAdddress));

    }

    @Test
    public void Rafa_t13_leer_usuario(){
// Url de esta petición: https://{{url_base}}/nga/api/v1{{account_id}}?lang=es

        RestAssured.baseURI = String.format("https://%s/nga/api/v1%s",base_url,account_id);

        Response response = given()
                .log()
                .all()
                .queryParam("lang","es")

                //HEADERS
                .header("Authorization","tag:scmcoord.com,2013:api " + token)
                .header("Origin","https://www.segundamano.mx")

                //METHOD:
                .get();

        // Definimos respuesta en string y headers
        String body_response = response.getBody().asString();
        String headers_response = response.getHeaders().toString();


        //impresiones que hace el sistema
        System.out.println("Body response:" + response.getStatusCode()); //con esto imprimimos la respuesta y el status code
        System.out.println("Body response: " + body_response);
        System.out.println("Headers response: " + headers_response);

        //PRUEBAS
        assertEquals(200,response.getStatusCode());
        assertNotNull(body_response);
        assertTrue(body_response.contains(account_id));

    }

    @Test
    public void Rafa_t14_obtener_categorias(){
        //Configurar URI
        RestAssured.baseURI = String.format("https://%s/nga/api/v1/public/categories/insert",base_url);
        //hacer el request y guardarlo en response



        Response response = given()
                .log()
                .all()
                .queryParam("lang","es")
                .get();

        String body_response = response.getBody().asString();
        String headers_response = response.getHeaders().toString();
        //String body_p = response.prettyPrint();
        System.out.println("Body response: " + body_response);
        System.out.println("Headers response: " + headers_response);
        //System.out.println("Body response: " + body_p);

        assertEquals(200,response.getStatusCode());
        assertNotNull(body_response);
        assertTrue(body_response.contains("categories"));

    }

    @Test
    public void Rafa_t15_leer_anuncios_pendientes(){
// Url de esta petición: https://{{url_base}}/nga/api/v1/private/accounts/5240470/klfst?status=active&lim=20&o=0&query=&lang=es

        RestAssured.baseURI = String.format("https://%s/nga/api/v1%s/klfst",base_url,account_id);

        Response response = given()
                .log()
                .all()
                .queryParam("status","pending")
                .queryParam("lim","20")
                .queryParam("o","0")
                .queryParam("query","")
                .queryParam("lang","es")

                //HEADERS
                .header("Authorization","tag:scmcoord.com,2013:api " + token)
                .header("Origin","https://www.segundamano.mx")

                //METHOD:
                .get();

        // Definimos respuesta en string y headers
        String body_response = response.getBody().asString();
        String headers_response = response.getHeaders().toString();


        //impresiones que hace el sistema
        System.out.println("Body response:" + response.getStatusCode()); //con esto imprimimos la respuesta y el status code
        System.out.println("Body response: " + body_response);
        System.out.println("Headers response: " + headers_response);

        //PRUEBAS
        assertEquals(200,response.getStatusCode());
        assertNotNull(body_response);
        assertTrue(body_response.contains("counter_map"));

    }

    @Test
    public void Rafa_t16_leer_anuncios_activos(){
// Url de esta petición: https://{{url_base}}/nga/api/v1/private/accounts/5240470/klfst?status=active&lim=20&o=0&query=&lang=es

        RestAssured.baseURI = String.format("https://%s/nga/api/v1%s/klfst",base_url,account_id);

        Response response = given()
                .log()
                .all()
                .queryParam("status","active")
                .queryParam("lim","20")
                .queryParam("o","0")
                .queryParam("query","")
                .queryParam("lang","es")

                //HEADERS
                .header("Authorization","tag:scmcoord.com,2013:api " + token)
                .header("Origin","https://www.segundamano.mx")

                //METHOD:
                .get();

        // Definimos respuesta en string y headers
        String body_response = response.getBody().asString();
        String headers_response = response.getHeaders().toString();


        //impresiones que hace el sistema
        System.out.println("Body response:" + response.getStatusCode()); //con esto imprimimos la respuesta y el status code
        System.out.println("Body response: " + body_response);
        System.out.println("Headers response: " + headers_response);

        //PRUEBAS
        assertEquals(200,response.getStatusCode());
        assertNotNull(body_response);
        assertTrue(body_response.contains("counter_map"));

    }

    @Test
    public void Rafa_t17_leer_alerta(){
    // Url de esta petición: https://{{url_base}}/alerts/v1/private/account/{{uuid}}/alert

        RestAssured.baseURI = String.format("https://%s/alerts/v1/private/account/%s/alert",base_url,uuid);

        Response response = given()
                .log()
                .all()

                //HEADERS
                .auth().preemptive().basic(uuid,token)
                .header("Origin", "https://www.segundamano.mx")

                //METHOD:
                .get();

        // Definimos respuesta en string y headers
        String body_response = response.getBody().asString();
        String headers_response = response.getHeaders().toString();


        //impresiones que hace el sistema
        System.out.println("Body response:" + response.getStatusCode()); //con esto imprimimos la respuesta y el status code
        System.out.println("Body response: " + body_response);
        System.out.println("Headers response: " + headers_response);

        //PRUEBAS
        assertEquals(200,response.getStatusCode());
        assertNotNull(body_response);
        assertTrue(body_response.contains(AlertID));

    }

    @Test
    public void Rafa_t18_borrar_alerta(){
        // Url de esta petición: https://{{url_base}}/alerts/v1/private/account/{{uuid}}/alert/{{AlertID}}

        RestAssured.baseURI = String.format("https://%s/alerts/v1/private/account/%s/alert/%s",base_url,uuid,AlertID);
        //String body ="{\"delete_reason\":{\"code\":\"0\"}}";

        Response response = given()
                .log()
                .all()

                //HEADERS
                .auth().preemptive().basic(uuid,token) //libreria rest assured que reemplaza el Basic {{ }}
                .header("Origin","https://www.segundamano.mx")
                .header("Accept","application/json, text/plain, */*")

                //BODY:
                //.body(body)

                //METHOD:
                .delete();

        // Definimos respuesta en string y headers
        String body_response = response.getBody().asString();
        String headers_response = response.getHeaders().toString();

        //impresiones que hace el sistema
        System.out.println("Body response:" + response.getStatusCode()); //con esto imprimimos la respuesta y el status code
        System.out.println("Body response: " + body_response);
        System.out.println("Headers response: " + headers_response);

        //PRUEBAS
        assertEquals(200,response.getStatusCode());
        assertNotNull(body_response);
        //assertTrue(body_response.contains(idAdddress));

    }

    @Test
    public void Rafa_t19_leer_favoritos(){
        // https://{{url_base}}/favorites/v1/private/accounts/{{uuid}}

        RestAssured.baseURI = String.format("https://%s/favorites/v1/private/accounts/%s",base_url,uuid);

        Response response = given()
                .log()
                .all()

                //HEADERS
                .auth().preemptive().basic(uuid,token)
                .header("Origin", "https://www.segundamano.mx")

                //METHOD:
                .get();

        // Definimos respuesta en string y headers
        String body_response = response.getBody().asString();
        String headers_response = response.getHeaders().toString();


        //impresiones que hace el sistema
        System.out.println("Body response:" + response.getStatusCode()); //con esto imprimimos la respuesta y el status code
        System.out.println("Body response: " + body_response);
        System.out.println("Headers response: " + headers_response);

        //PRUEBAS
        assertEquals(200,response.getStatusCode());
        assertNotNull(body_response);
        assertTrue(body_response.contains("Favoritos"));

    }

    @Test
    public void Rafa_t20_borrar_favorito(){
        // Url de esta petición: https://{{url_base}}/favorites/v1/private/accounts/{{uuid}}

        RestAssured.baseURI = String.format("https://%s/favorites/v1/private/accounts/%s",base_url,uuid);
        String body ="{\"list_ids\":[937412221]}";

        Response response = given()
                .log()
                .all()

                //HEADERS
                .auth().preemptive().basic(uuid,token) //libreria rest assured que reemplaza el Basic {{ }}
                .header("Origin","https://www.segundamano.mx")
                .header("Content-Type","application/json")

                //BODY:
                .body(body)

                //METHOD:
                .delete();

        // Definimos respuesta en string y headers
        String body_response = response.getBody().asString();
        String headers_response = response.getHeaders().toString();

        //impresiones que hace el sistema
        System.out.println("Body response:" + response.getStatusCode()); //con esto imprimimos la respuesta y el status code
        System.out.println("Body response: " + body_response);
        System.out.println("Headers response: " + headers_response);

        //PRUEBAS
        assertEquals(200,response.getStatusCode());
        assertNotNull(body_response);
        //assertTrue(body_response.contains(idAdddress));

    }

    @Test
    public void Rafa_t21_leer_balance_de_monedas(){
        //https://{{url_base}}/nga/api/v1/api/users/{{uuid}}/counter?la\g=es

        RestAssured.baseURI = String.format("https://%s/nga/api/v1/api/users/%s/counter",base_url,uuid);

        Response response = given()
                .log()
                .all()
                .queryParam("la\\g","es")

                //HEADERS
                .header("Authorization","tag:scmcoord.com,2013:api " + token)
                .header("Origin", "https://www.segundamano.mx")
                .header("Accept", "application/json, text/plain, */*")

                //METHOD:
                .get();

        // Definimos respuesta en string y headers
        String body_response = response.getBody().asString();
        String headers_response = response.getHeaders().toString();


        //impresiones que hace el sistema
        System.out.println("Body response:" + response.getStatusCode()); //con esto imprimimos la respuesta y el status code
        System.out.println("Body response: " + body_response);
        System.out.println("Headers response: " + headers_response);

        //PRUEBAS
        assertEquals(200,response.getStatusCode());
        assertNotNull(body_response);
        assertTrue(body_response.contains("unread"));

    }

    @Test
    public void Rafa_t22_actualizar_nombre_telefono_professional(){
        // https://{{url_base}}/nga/api/v1{{account_id}}?lang=es

        RestAssured.baseURI = String.format("https://%s/nga/api/v1%s",base_url,account_id);
        String body = "{\"account\":{\"name\":\"Rafaeliño Gudiño Sosa\",\"phone\":\"3322332233\",\"professional\":false}}";

        Response response = given()
                .log()
                .all()
                .queryParam("lang","es")

                //HEADERS
                .header("Authorization","tag:scmcoord.com,2013:api " + token)
                .header("Origin", "https://www.segundamano.mx")
                .header("Accept", "application/json, text/plain, */*")

                //BODY
                .body(body)

                //METHOD:
                .patch();

        // Definimos respuesta en string y headers
        String body_response = response.getBody().asString();
        String headers_response = response.getHeaders().toString();


        //impresiones que hace el sistema
        System.out.println("Body response:" + response.getStatusCode()); //con esto imprimimos la respuesta y el status code
        System.out.println("Body response: " + body_response);
        System.out.println("Headers response: " + headers_response);

        //PRUEBAS
        assertEquals(200,response.getStatusCode());
        assertNotNull(body_response);
        assertTrue(body_response.contains(account_id));

    }

    @Test
    public void Rafa_t23_actualizar_password(){
        // https://{{url_base}}/nga/api/v1{{account_id}}?lang=es

        RestAssured.baseURI = String.format("https://%s/nga/api/v1%s",base_url,account_id);
        String body = "{\"account\":{\"password\":\"Sopral0408\\u0021\"}}\n";

        Response response = given()
                .log()
                .all()
                .queryParam("lang","es")

                //HEADERS

                .header("Content-Type","application/json")
                .header("Authorization","tag:scmcoord.com,2013:api " + token)
                .header("Origin", "https://www.segundamano.mx")
                .header("Accept", "application/json, text/plain, */*")

                //BODY
                .body(body)

                //METHOD:
                .patch();

        // Definimos respuesta en string y headers
        String body_response = response.getBody().asString();
        String headers_response = response.getHeaders().toString();


        //impresiones que hace el sistema
        System.out.println("Body response:" + response.getStatusCode()); //con esto imprimimos la respuesta y el status code
        System.out.println("Body response: " + body_response);
        System.out.println("Headers response: " + headers_response);

        //PRUEBAS
        assertEquals(200,response.getStatusCode());
        assertNotNull(body_response);
        assertTrue(body_response.contains(account_id));

    }

}
