package com.shashov.currency.common;

import android.support.annotation.NonNull;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.core.Persister;

import java.util.ArrayList;
import java.util.List;

public class CurrenciesXmlParser {

    public static List<Currency> parseCurrencies(@NonNull String xmlResult) throws Exception {
        List<Currency> result = new ArrayList<>();
        Response response = new Persister().read(Response.class, xmlResult);
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
}
