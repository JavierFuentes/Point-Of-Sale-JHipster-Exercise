package asv.exercise.service.util;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by javier on 31/01/16.
 *
 * POJO to retrieve online Barcodes description from
 * http://upcdatabase.org/api
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class UpcDatabase {

    private String valid;
    private String number;
    private String itemname;
    private String alias;
    private String description;
    private String avg_price;
    private String rate_up;
    private String rate_down;

    public UpcDatabase() {

    }

    public String getValid() {
        return valid;
    }

    public void setValid( String valid ) {
        this.valid = valid;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber( String number ) {
        this.number = number;
    }

    public String getItemname() {
        return itemname;
    }

    public void setItemname( String itemname ) {
        this.itemname = itemname;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias( String alias ) {
        this.alias = alias;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription( String description ) {
        this.description = description;
    }

    public String getAvg_price() {
        return avg_price;
    }

    public void setAvg_price( String avg_price ) {
        this.avg_price = avg_price;
    }

    public String getRate_up() {
        return rate_up;
    }

    public void setRate_up( String rate_up ) {
        this.rate_up = rate_up;
    }

    public String getRate_down() {
        return rate_down;
    }

    public void setRate_down( String rate_down ) {
        this.rate_down = rate_down;
    }

    @Override
    public String toString() {
        return "UpdDatabase{" +
                "valid=" + valid +
                ", number='" + number + "'" +
                ", itemname='" + itemname + "'" +
                ", alias='" + alias + "'" +
                ", description='" + description + "'" +
                ", avg_price='" + avg_price + "'" +
                ", rate_up='" + rate_up + "'" +
                ", rate_down='" + rate_down + "'" +
                '}';
    }

}
