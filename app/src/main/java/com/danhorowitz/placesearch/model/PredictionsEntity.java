
package com.danhorowitz.placesearch.model;

import java.util.List;

public class PredictionsEntity {
    /**
     * description : Barcelona, Spain
     * id : 5695851cee37adbcea7305c0473a15906dbcab8f
     * matched_substrings : [{"length":9,"offset":0}]
     * place_id : ChIJ5TCOcRaYpBIRCmZHTz37sEQ
     * reference : CjQoAAAAXRJHHaGKQDWsSW8-OhfEgcvKCitTyyNOWuRn0y-Y9AMxli2dTdclwYDKTewyZn38EhCjdvRFVbWjZ-VmvRvbJCXVGhRgFra4T_wN7wt5dPPPmHyU2wx2wg
     * terms : [{"offset":0,"value":"Barcelona"},{"offset":11,"value":"Spain"}]
     * types : ["locality","political","geocode"]
     */

    private String description;
    private String id;
    private String place_id;
    private String reference;
    private List<MatchedSubstringsEntity> matched_substrings;
    private List<TermsEntity> terms;
    private List<String> types;

    public void setDescription(String description) {
        this.description = description;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setPlace_id(String place_id) {
        this.place_id = place_id;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public void setMatched_substrings(List<MatchedSubstringsEntity> matched_substrings) {
        this.matched_substrings = matched_substrings;
    }

    public void setTerms(List<TermsEntity> terms) {
        this.terms = terms;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }

    public String getDescription() {
        return description;
    }

    public String getId() {
        return id;
    }

    public String getPlace_id() {
        return place_id;
    }

    public String getReference() {
        return reference;
    }

    public List<MatchedSubstringsEntity> getMatched_substrings() {
        return matched_substrings;
    }

    public List<TermsEntity> getTerms() {
        return terms;
    }

    public List<String> getTypes() {
        return types;
    }

    public static class MatchedSubstringsEntity {
        /**
         * length : 9
         * offset : 0
         */

        private int length;
        private int offset;

        public void setLength(int length) {
            this.length = length;
        }

        public void setOffset(int offset) {
            this.offset = offset;
        }

        public int getLength() {
            return length;
        }

        public int getOffset() {
            return offset;
        }
    }

    public static class TermsEntity {
        /**
         * offset : 0
         * value : Barcelona
         */

        private int offset;
        private String value;

        public void setOffset(int offset) {
            this.offset = offset;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public int getOffset() {
            return offset;
        }

        public String getValue() {
            return value;
        }
    }
}