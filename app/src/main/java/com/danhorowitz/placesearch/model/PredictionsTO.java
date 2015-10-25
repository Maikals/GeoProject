
package com.danhorowitz.placesearch.model;

import java.util.List;

/**
 * Created by dhorowitz on 18/09/2015.
 */
public class PredictionsTO {

    /**
     * predictions : [{"description":"Barcelona, Spain","id":"5695851cee37adbcea7305c0473a15906dbcab8f","matched_substrings":[{"length":9,"offset":0}],"place_id":"ChIJ5TCOcRaYpBIRCmZHTz37sEQ","reference":"CjQoAAAAXRJHHaGKQDWsSW8-OhfEgcvKCitTyyNOWuRn0y-Y9AMxli2dTdclwYDKTewyZn38EhCjdvRFVbWjZ-VmvRvbJCXVGhRgFra4T_wN7wt5dPPPmHyU2wx2wg","terms":[{"offset":0,"value":"Barcelona"},{"offset":11,"value":"Spain"}],"types":["locality","political","geocode"]},{"description":"Barcelona-Sants, Barcelona, Spain","id":"01e453a4a3431e506b87aa1c2f731db2f280f8f0","matched_substrings":[{"length":9,"offset":0}],"place_id":"ChIJAYHo0oCYpBIRFcBa_YSqkVk","reference":"CjQvAAAAsNgJMKMnO7FASMsQeme1FoaD4xwXXb-Gm-4RtxwVwjRLCqAnD2QbjLh_AOJYQ5-dEhD764zKF-VP6L-jYK2HC4e2GhRKDiNn1OKFgcp8PCZws3rEcFmiZg","terms":[{"offset":0,"value":"Barcelona-Sants"},{"offset":17,"value":"Barcelona"},{"offset":28,"value":"Spain"}],"types":["transit_station","point_of_interest","establishment","geocode"]},{"description":"Barcelona Nord, Barcelona, Spain","id":"ec357f122ef70c6a73fa85f8a17afacfb3f4982e","matched_substrings":[{"length":9,"offset":0}],"place_id":"ChIJe5fWGeKipBIRa1CjdvM7EXA","reference":"CjQuAAAA_v6j60ILHmWtOfslFwnWHR2MOHSg-JKAQERfi7THJcwXm-P0k37hpsXf7d5EWFafEhB97ZCxOXeJ8QoBD7XqijnDGhRbV-14JUZCwDy4DXE14EjTp4GFCQ","terms":[{"offset":0,"value":"Barcelona Nord"},{"offset":16,"value":"Barcelona"},{"offset":27,"value":"Spain"}],"types":["transit_station","point_of_interest","establishment","geocode"]},{"description":"Barcelona-Clot-Arago, Barcelona, Spain","id":"87d91bbe42e52e5e393c11bfc4cf3a76b1dca4bd","matched_substrings":[{"length":9,"offset":0}],"place_id":"ChIJh038SyajpBIRlZzI5I6YBSo","reference":"CkQ0AAAAuSAvm1GwX5KB_9nzNyZASP1MO3GeXk9a-apuVLrdY5EerCMMMUPhRxo8fJzCHOFPpjtuHcwR0aKT3_7fNCyK2hIQHZDFfehogO6iaPr9arq28BoUSJYOSqXcP_NPtmaK_vDukvpAOlU","terms":[{"offset":0,"value":"Barcelona-Clot-Arago"},{"offset":22,"value":"Barcelona"},{"offset":33,"value":"Spain"}],"types":["transit_station","point_of_interest","establishment","geocode"]},{"description":"Barcelona Cathedral, Carrer del Bisbe, Barcelona, Spain","id":"5f611419c239e6de2ba3ef06ba5c78c561413245","matched_substrings":[{"length":9,"offset":0}],"place_id":"ChIJsYUkpPmipBIRuykk5iJXUME","reference":"ClRPAAAAOdp5M-Ld6Hg80OzVDw5E01jfWBpMS-7Jsuikobm7YbJNlxgt4pwLkVVYp6QyYrJQi05P-QAO8oH3_7vfLGDMaBaO8j-gnbU_jRGuBEvVNMoSEOQ-HzpPJEVeEX6XR3_1ZWoaFFFAJT8rR_KHeWRtP9HqvQs_59vy","terms":[{"offset":0,"value":"Barcelona Cathedral"},{"offset":21,"value":"Carrer del Bisbe"},{"offset":39,"value":"Barcelona"},{"offset":50,"value":"Spain"}],"types":["premise","geocode"]}]
     * status : OK
     */

    private String status;
    private List<PredictionsEntity> predictions;

    public void setStatus(String status) {
        this.status = status;
    }

    public void setPredictions(List<PredictionsEntity> predictions) {
        this.predictions = predictions;
    }

    public String getStatus() {
        return status;
    }

    public List<PredictionsEntity> getPredictions() {
        return predictions;
    }


}
