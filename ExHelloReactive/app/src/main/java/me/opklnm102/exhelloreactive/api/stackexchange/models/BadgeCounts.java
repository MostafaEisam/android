package me.opklnm102.exhelloreactive.api.stackexchange.models;

import com.google.gson.annotations.Expose;

/**
 * Created by Administrator on 2016-05-12.
 */
public class BadgeCounts {

    @Expose
    private Integer bronze;

    @Expose
    private Integer silver;

    @Expose
    private Integer gold;

    public Integer getBronze() {
        return bronze;
    }

    public void setBronze(Integer bronze) {
        this.bronze = bronze;
    }

    public Integer getSilver() {
        return silver;
    }

    public void setSilver(Integer silver) {
        this.silver = silver;
    }

    public Integer getGold() {
        return gold;
    }

    public void setGold(Integer gold) {
        this.gold = gold;
    }
}
