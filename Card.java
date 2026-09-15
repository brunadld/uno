public class Card {

    private final String value;
    private String color;

    public Card(String value, String color) {
        if(color.equals("Null")) {
            this.color = null;
        }
        else {
            this.color = color;
        }
        
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

    public String getColor() {
        if(this.color == null) {
            return "";
        }
        return this.color;
    }

    public void setColor(String color) {
        this.color = color;
    }

}