package galodamadrugada.onhere.domain;

/**
 * Created by UNIVASF on 30/03/2017.
 */

public class Event {

    private String nomeEvento;
    private String dataEvento;

    public Event(){}

    public Event(String nome, String data ){
        this.nomeEvento = nome;
        this.dataEvento = data;
    }

    public String getNomeEvento() {
        return nomeEvento;
    }

    public void setNomeEvento(String nomeEvento) {
        this.nomeEvento = nomeEvento;
    }

    public String getDataEvento() {
        return dataEvento;
    }

    public void setDataEvento(String dataEvento) {
        this.dataEvento = dataEvento;
    }
}
