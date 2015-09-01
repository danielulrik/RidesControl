package com.ulrik.rides.service;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.*;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Created with IntelliJ IDEA.
 * User: Leonardo Neves
 * Date: 27/03/2015
 * Time: 15:09
 */
public class ParserJson {

    private static FileWriter file;

    public ParserJson() {
    }

    public String serializar(Object obj, Type type) {
        Gson g = new GsonBuilder().registerTypeAdapterFactory(MyDateGsonAdapter.FACTORY).addSerializationExclusionStrategy(new ExclusionStrategy() {
            @Override
            public boolean shouldSkipField(FieldAttributes fieldAttributes) {
                return false;
            }

            @Override
            public boolean shouldSkipClass(Class<?> aClass) {

                return false;
            }
        }).excludeFieldsWithModifiers(Modifier.TRANSIENT | Modifier.STATIC).create();
        return g.toJson(obj, type);
    }

    public File serializar(Object o, File file, Type type) {
        Gson g = new GsonBuilder().registerTypeAdapterFactory(MyDateGsonAdapter.FACTORY).excludeFieldsWithModifiers(Modifier.TRANSIENT).create();
        String je = g.toJson(o, type);
        FileWriter fw = null;
        try {
            fw = new FileWriter(file, true);
            fw.write(je);
        } catch (IOException e) {

        } finally {
            try {
                fw.close();
            } catch (IOException e) {

            }
        }
        return file;
    }


    public <T> Object descerializar(T obj, InputStream dados, TypeAdapterFactory... typeAdapterFactories) {
        GsonBuilder g = new GsonBuilder().registerTypeAdapterFactory(MyDateGsonAdapter.FACTORY).excludeFieldsWithModifiers(Modifier.TRANSIENT);
        Reader r = new InputStreamReader(dados);
        for (TypeAdapterFactory t : typeAdapterFactories) {
            g.registerTypeAdapterFactory(t);
        }
        Type t = (Type) obj;
        if (t == null) {
            throw new NullPointerException("tipo: " + obj.getClass() + " não encontrado.");
        }
        return g.create().fromJson(r, t);
    }

    public <T> Object descerializar(Type type, InputStream dados, TypeAdapterFactory... typeAdapterFactories) {
        GsonBuilder g = new GsonBuilder().registerTypeAdapterFactory(MyDateGsonAdapter.FACTORY).excludeFieldsWithModifiers(Modifier.TRANSIENT);
        for (TypeAdapterFactory t : typeAdapterFactories) {
            g.registerTypeAdapterFactory(t);
        }
        Reader r = new InputStreamReader(dados);
        return g.create().fromJson(r, type);
    }

    public Object descerializar(Type type, String dados) {
        Gson g = new GsonBuilder().registerTypeAdapterFactory(MyDateGsonAdapter.FACTORY).excludeFieldsWithModifiers(Modifier.TRANSIENT).create();
        return g.fromJson(dados, type);
    }




    public static class MyDateGsonAdapter extends TypeAdapter<Date> {
        public static final TypeAdapterFactory FACTORY = new TypeAdapterFactory() {
            @SuppressWarnings("unchecked") // we use a runtime check to make sure the 'T's equal
            public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
                return typeToken.getRawType() == Date.class ? (TypeAdapter<T>) new MyDateGsonAdapter() : null;
            }
        };
        private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

        @Override
        public void write(JsonWriter out, Date value) throws IOException {
            if (value == null) {
                out.nullValue();
                return;
            }
            String dateFormatAsString = sdf.format(value);
            out.value(dateFormatAsString);
        }


        @Override
        public Date read(JsonReader in) throws IOException {
            if (in.peek() == JsonToken.NULL) {
                in.nextNull();
                return null;
            }
            try {
                return sdf.parse(in.nextString());
            } catch (ParseException e) {

                return new Date(0);
            }
        }
    }




}
