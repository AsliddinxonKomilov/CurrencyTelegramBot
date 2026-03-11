package currencyBot.model;

import com.google.gson.annotations.SerializedName;

public class CurrencyResponse {
        @SerializedName("Ccy")
        public String code;


        @SerializedName("CcyNm_UZ")
        public String name;


        @SerializedName("Rate")
        public String rate;


        @SerializedName("Diff")
        public String diff;
    }