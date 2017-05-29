package com.clienteHttp;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


import java.io.IOException;
import java.util.ArrayList;



/**
 * Created by MT on 24/5/2017.
 */
public class Main {

    public static void main(String[] args) throws IOException {


        String url = capturarURL();
        Document doc = Jsoup.connect(url).get();

        //a)Obtener lineas.

        int lineas = doc.html().split("\n").length;
        System.out.println("a) Lineas: " + lineas);

        //b) Obtener parrafos.

       // Elements parrafos = doc.getElementsByTag("p"); //OTRA OPCION PARA RELIZAR SELECT
        Elements parrafos = doc.select("p");
        System.out.println("b) Parrafos: " + parrafos.size());

        //c) Obtener imagenes.
        Elements imagenes = doc.getElementsByTag("p").select("img");
        System.out.println("c) Imagenes: " + imagenes.size());

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

        //e) Imputs cada formulario.

        System.out.println("e)");
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

        //Realizar POST enviando parametros.

        //Elements formularios = doc.select("form");
        System.out.println("\n\nf)");
        for(Element form : formularios){

            String formAction = form.absUrl("action"), formMethod = form.attr("method");

            if( formAction == "" ){
                formAction = url;
            }

            formMethod = formMethod.toLowerCase();

            if( formMethod.equals("post") ){

                String userAgent = "Chromium";
                Document d = null;
                try {
                    d = Jsoup.connect(formAction).data("asignatura", "practica1").userAgent(userAgent).post();
                    String salida = d.outerHtml();
                    System.out.println(salida);
                } catch (Exception e1) {
                    System.out.println("Fallo" + formAction);
                }

            }
        }


        //IMPRIME DOC HTML
        //System.out.println("\n\n" + "DOCUMENTO HTML: \n" + doc.toString());//IMPRIME EL DOCUMENTO HTML

    }
    private static String capturarURL(){
        String url;

        /*
        Scanner scanner = new Scanner(System.in);
        String[] schemes = {"http", "https"};
        UrlValidator urlValidator = new UrlValidator(schemes);

        do {
            System.out.print("Digite la URL:");
            url = scanner.next();
            if (!urlValidator.isValid(url))
                System.out.println("ERROR!!!\n");
        } while (!urlValidator.isValid(url));
        scanner.close();
        */
        //url="http://localhost:4567";
        url="http://itachi.avathartech.com:4567/2017.html";

        return url;
    }
}
