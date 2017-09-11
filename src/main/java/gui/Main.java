package gui;

import controller.EntityManagerUtils;

/**
 * Created by MaxPower on 09/09/2017.
 */
public class Main {

    public static void main(String[] args) {

        EntityManagerUtils emu = new EntityManagerUtils();

        emu.saveClient();
        //emu.deleteClient(1);
        System.out.println(emu.findClient(1));
    }
}
