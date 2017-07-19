package com.shashov.currency.common;

import android.support.annotation.NonNull;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.core.Persister;
import org.simpleframework.xml.transform.RegistryMatcher;
import org.simpleframework.xml.transform.Transform;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CurrenciesXmlParser {

    public static List<Currency> parseCurrencies(@NonNull String xmlResult) throws Exception {
        List<Currency> result = new ArrayList<>();
        RegistryMatcher m = new RegistryMatcher();
        m.bind(Double.class, new FloatNumberMatcher(new Locale("ru", "RU")));
        Response response = new Persister(m).read(Response.class, xmlResult);
        result.addAll(response.getList());
        return result;
    }

    @Root(name = "ValCurs", strict = false)
    public static class Response {
        @ElementList(inline = true)
        private List<Currency> list;

        public Response() {
        }

        public List<Currency> getList() {
            return list;
        }

        public void setList(@NonNull List<Currency> list) {
            this.list = list;
        }
    }

    private static class FloatNumberMatcher implements Transform<Double> {
        private final NumberFormat numberFormat;

        public FloatNumberMatcher(Locale locale) {
            numberFormat = NumberFormat.getInstance(locale);
        }

        @Override
        public Double read(@NonNull String value) throws Exception {
            return numberFormat.parse(value).doubleValue();
        }

        @Override
        public String write(@NonNull Double value) throws Exception {
            return numberFormat.format(value);
        }

    }
}
