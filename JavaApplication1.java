/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication1;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 *
 */
public class JavaApplication1 {

    public static void main(String[] args) {
        // Using Scanner for Getting Input from User 
        Scanner in = new Scanner(System.in);

        while (true) {
            System.out.println("exit? if yes, type yes/y, if no, just enter");
            String line = in.nextLine();
            if (line.toLowerCase().matches("yes|y")) {
                break;
            }
            configDevice(in);
        }

        //modifyFile("C:/StudentFile.txt", "85", "95");
    }

    public static void configDevice(Scanner in) {
        System.out.println("what type of device you are configuring: 1.pc, 2.switch, 3.route, 4.multi-level-switch");
        String deviceType = in.nextLine();
        int deviceTypeInt = Integer.parseInt(deviceType);

        System.out.println("configuring: " + DeviceTypeEnum.values()[deviceTypeInt - 1]);

        switch (deviceTypeInt) {
            case 1: //PC
                ConfigUtil.configPC(in);
                break;
                
            case 2:// switch
                ConfigUtil.configSwitch(in);
                break;
                
            case 3: //route
                ConfigUtil.configRoute(in);
                break;

            case 4: //multi-level-switch
                ConfigUtil.configMultiLayerSwitch(in);
                break;
                
            default:
                System.out.println("Wrong input value, please input only int in {1,2,3,4}");
                break;

        }

    }

}
