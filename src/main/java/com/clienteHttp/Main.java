package com.clienteHttp;

import org.apache.commons.validator.routines.UrlValidator;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;


/**
 * Created by MT on 24/5/2017.
 */
public class Main {
    public static void main(String[] args) throws IOException {

        //String url="http://localhost:4567";
        //String url="https://github.com";
        //String url = capturarURL();
        String url="https://www.spotify.com/do/signup?asignatura=practica1";

        Document doc = Jsoup.connect(url).get();

        //a)Obtener lineas.
        int lineas = doc.html().split("\n").length;
        System.out.println("a) Lineas: " + lineas);

        //b) Obtener parrafos.
       // Elements parrafos = doc.getElementsByTag("p");
        Elements parrafos = doc.select("p");
        System.out.println("b) Parrafos: " + parrafos.size());

        //c) Obtener imagenes.
        int img=0;
        if(doc.getElementsByTag("p").size()!=0){

            for(Element imagenes : doc.getElementsByTag("p").iterator().next().children()){
                if(imagenes.tagName().equals("img")){
                    img++;
                }
            }
            //Elements imagenes = doc.getElementsByTag("img");
        }
        System.out.println("c) Imagenes: " + img);

        //d) Obtener formularios.
        Elements formularios = doc.getElementsByTag("form");
        int post=0, get=0;
        System.out.println("d) Total de Formularios: " + formularios.size());
        for(int i=0; i<formularios.size(); i++){
            if(formularios.get(i).attr("method").equals("post")){
                post++;
            }
            else if(formularios.get(i).attr("method").equals("get")){
                get++;
            }
        }
        System.out.println("\tFormularios POST:"+ post + "\n\tFormularios GET:" + get);

        //e) Imputs C/ formulario.
        ArrayList<Elements> inputs = new ArrayList<Elements>();

        for (Element form : formularios) {
            inputs.add(form.getElementsByTag("input"));
        }

        int count = 1;
        for (Elements items : inputs) {
            System.out.println("\nFormulario #" + count + ": ");
            for (Element input : items) {
                String type = input.attr("type");
                System.out.println("Tipo: " + type + " - Campo: " + input);
            }
            count++;
        }

        //f)

        /*
        for(int i = 0; i < formularios.size();i++){
            Element e = formularios.get(i);
            //System.out.println("\tEl formulario utiliza el metodo: " + e.attr("method"));
            if(e.attr("method").toLowerCase().equals("post")){
                System.out.println("\tEl Formulario #"+i+":");
                //Connection.Response res = Jsoup.connect(e.attr("action")).data("name","practica1").method(Connection.Method.POST).execute();
                //System.out.println("\t" + res);

            }
        }
        */

        respuestaServer(doc,url);


        //IMPRIME DOC HTML
        //System.out.println("\n\n" + "DOCUMENTO HTML: \n" + doc.toString());//IMPRIME EL DOCUMENTO HTML

    }
    private static void respuestaServer(Document doc,String url) throws IOException {
        Elements forms = doc.getElementsByTag("form");
        String attrib=forms.attr("method");
        //System.out.println(attrib);
        if(attrib.equals("post")|| attrib.equals("POST")){
            String action=forms.attr("action");
            //Connection.Response response=Jsoup.connect(action).data("asignatura","practica1").userAgent("Chromium").ignoreHttpErrors(true).method(Connection.Method.POST).execute();
            if(action.contains("http")){
                Connection.Response response=Jsoup.connect(action).data("asignatura","practica1").userAgent("Chromium").ignoreHttpErrors(true).method(Connection.Method.POST).execute();
                System.out.println("\nEl server dio como respuesta el Codigo-> "+response.statusCode());
                System.out.println(response.parse());

                if(response.statusCode()==200){
                    System.out.println("\nENVIO SATISFACTORIO");
                }
            }else {
                Connection.Response response=Jsoup.connect(url+action).data("asignatura","practica1").userAgent("Chromium").ignoreHttpErrors(true).method(Connection.Method.POST).execute();
                System.out.println("\nEl server dio como respuesta el Codigo-> "+response.statusCode());
                System.out.println(response.parse());
                if(response.statusCode()==200){
                    System.out.println("\nENVIO SATISFACTORIO");
                }
            }

        }
    }

    public static String capturarURL(){
        Scanner scanner = new Scanner(System.in);                   //QUE ES SCANER?
        String[] schemes = {"http", "https"};                        //OJO
        UrlValidator urlValidator = new UrlValidator(schemes);      //Verifica que URL tiene la sintaxis correcta
        String url;
        do {
          System.out.print("Digite la URL:");
          url = scanner.next();
          if (!urlValidator.isValid(url))
          System.out.println("ERROR!!!\n");
        } while (!urlValidator.isValid(url));
        scanner.close();

        return url;
    }

}
