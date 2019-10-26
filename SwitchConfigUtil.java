/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication1;

import java.util.List;
import java.util.Map;
import java.util.Scanner;
import static javaapplication1.ConfigUtil.ROOT_TO_FILE_PATH;

/**
 *
 */
public class SwitchConfigUtil {

    static void configSwitch(Scanner in) {
        //require hostname
        String hostNameStr = "";
        do {
            System.out.println("input switch hostname: ");
            hostNameStr = in.nextLine();
        } while (hostNameStr.isEmpty());
        System.out.println("---hostname: " + hostNameStr);

        String toFileName = hostNameStr + ".txt";

        String toFile = ROOT_TO_FILE_PATH + toFileName;

        //build the replacement keywords map, then process replacement
        ConfigFilesGenerator helper = new ConfigFilesGenerator(toFile);
        List<String> appendlineList = helper.getAppendLinesList();

        /*
        en
        conf t
        hostname [name]
         */
        appendlineList.add("en");
        appendlineList.add("conf t");
        appendlineList.add("hostname " + hostNameStr);

        //step 1: basic config
        configBasic(in, appendlineList);

        //step 2: config vlans and their int vlans
        configVlans(in, appendlineList);

        //step 3: config ports
        configPorts(in, appendlineList);

        //step 4: config options
        configOptions(in, appendlineList);

        //last but not least
        helper.generateConfigFile();
    }

    private static void configBasic(Scanner in, List<String> appendlineList) {

        CommandUtil.doBannerConfig(in, appendlineList);
        CommandUtil.doEnablePswConfig(in, appendlineList);
        CommandUtil.doEnableSecretConfig(in, appendlineList);

        appendlineList.add("service password-encryption");

        CommandUtil.doDefaultGatewayConfig(in, appendlineList);
        CommandUtil.doDnsServerConfig(in, appendlineList);
    }

    private static void configPorts(Scanner in, List<String> appendlineList) {
        System.out.println("=== start to config ports ===");
        String input = "";
        do {
            configPort(in, appendlineList);
            System.out.println("more interface? if no, just press enter, if yes, enter any other key");
            input = in.nextLine();

        } while (!input.isEmpty());

    }

    private static void configPort(Scanner in, List<String> appendlineList) {
        //Step 3 - part 1
        CommandUtil.doInterfaceConfig(in, appendlineList);

        System.out.println("port on/off? if off, just enter. if on, enter any other key");
        String inputStr = in.nextLine();
        if (!inputStr.isEmpty()) {  //port is on

            //config Trunk or access
            System.out.println("config trunk? if no, just enter. if yes, enter any key");
            inputStr = in.nextLine();
            if (!inputStr.isEmpty()) { // yes trunk
                //Step 3 - Part T
                configTrunk(in, appendlineList);
            } else { // not trunk
                System.out.println("config access? if no, just enter. if yes, enter any key");
                inputStr = in.nextLine();
                if (!inputStr.isEmpty()) { // yes access
                    //Step 3 - Part A
                    configAccess(in, appendlineList);
                }
            }

            //config port security
            System.out.println("config port security? if no, just enter. if yes, enter any key");
            inputStr = in.nextLine();
            if (!inputStr.isEmpty()) { // yes port security
                //Step 3 - Part 2
                configPortSecurity(in, appendlineList);
            }

            //Step 3 - Part 3:
            appendlineList.add("no shutdown");
        } else {  //port is off
            //Step 3 - Part 4
            appendlineList.add("shutdown");
        }
        appendlineList.add("exit");

    }

    private static void configPortSecurity(Scanner in, List<String> appendlineList) {
        //switchport port-security
        //switchport port-security maximum [amount]
        //switchport port-security mac-address sticky
        //switchport port-security violation [protect/restrict/shutdown]

        appendlineList.add("switchport port-security");

        CommandUtil.doPortSecMaxConfig(in, appendlineList);

        appendlineList.add("switchport port-security mac-address sticky");

        CommandUtil.doPortSecViolationConfig(in, appendlineList);

    }

    //Step 3 - Part A
    private static void configAccess(Scanner in, List<String> appendlineList) {
        //switchport mode access
        //switchport access vlan [vlan number]

        appendlineList.add("switchport mode access");
        CommandUtil.doAccessVlanConfig(in, appendlineList);
    }

    //Step 3 - Part T
    private static void configTrunk(Scanner in, List<String> appendlineList) {
        //switchport mode trunk
        //switchport nonegotiate
        //switchport trunk allowed vlan [vlan number]
        //switchport trunk native vlan [native vlan number](optional)
        appendlineList.add("switchport mode trunk");
        appendlineList.add("switchport nonegotiate");
        CommandUtil.doTrunkAllowedVlanConfig(in, appendlineList);
        CommandUtil.doTrunkNativeVlanConfig(in, appendlineList);
    }

    private static void configVlans(Scanner in, List<String> appendlineList) {
        while (true) {
            System.out.println("config Vlan? if yes, input vlan number, if no, just press enter");
            String vlanNo = in.nextLine();
            if (vlanNo.isEmpty()) {
                System.out.println("---No more Vlan Config");
                break;
            } else {
                /*
                vlan [vlan number]
                name [vlan name]
                exit
                interface vlan [vlan number]
                ip address [ip address] [subnet mask] (optional)
                no shutdown
                exit
                 */
                System.out.println("---Vlan number:" + vlanNo);
                appendlineList.add("vlan " + vlanNo);
                CommandUtil.doVlanNameConfig(in, appendlineList);
                appendlineList.add("exit");

                //config interface vlan
                appendlineList.add("interface vlan " + vlanNo);

                //Step 2 - Part 2
                CommandUtil.doVlanIpConfig(in, appendlineList);

                //Step 2 - Part 3
                appendlineList.add("no shutdown");
                appendlineList.add("exit");
            }
        }
    }

    //Step 4
    private static void configOptions(Scanner in, List<String> appendlineList) {
        System.out.println("line console config? if no, just enter. if yes, hit any other key ");
        String inputStr = in.nextLine();
        if (!inputStr.isEmpty()) {  // yes, need config console
            configConsole(in, appendlineList);
            System.out.println("---console config: yes ");
        }

        System.out.println("telnet config? if no, just enter. if yes, hit any other key ");
        inputStr = in.nextLine();
        if (!inputStr.isEmpty()) {  // yes, need telnet config
            System.out.println("---telnet config: yes ");
            configTelnet(in, appendlineList);
        } else {//not telnet
            System.out.println("SSH config? if no, just enter. if yes, hit any other key ");
            inputStr = in.nextLine();
            if (!inputStr.isEmpty()) {  // yes, need ssh config
                System.out.println("---ssh config: yes ");
                configSsh(in, appendlineList);
            }
        }
    }

    //Step 4 - Part C
    private static void configConsole(Scanner in, List<String> appendlineList) {
        //line console [console number] (default: 0)
        //password [password] (required)
        //login
        //exit
        CommandUtil.doLineConsoleConfig(in, appendlineList);
        CommandUtil.doPasswordConfig(in, appendlineList);
        appendlineList.add("login");
        appendlineList.add("exit");
    }

    //Step 4 - Part T
    private static void configTelnet(Scanner in, List<String> appendlineList) {
        //line vty [first number] [last number] (default: 0 15)
        //exec-timeout [timeout time in minutes] (optional)
        //password [password] (required)
        //transport input telnet
        //login
        //exit

        CommandUtil.doLineVtyConfig(in, appendlineList);
        CommandUtil.doExecTimeoutConfig(in, appendlineList);
        CommandUtil.doPasswordConfig(in, appendlineList);
        appendlineList.add("transport input telnet");
        appendlineList.add("login");
        appendlineList.add("exit");
    }

    //Step 4 - Part S
    private static void configSsh(Scanner in, List<String> appendlineList) {
        /*
        username [username] secret [secret]
        (alternative) username [username] password [password]
        ip domain-name [domain name]
        crypto key generate rsa [bits] (default: 1024)
        line vty [first number] [last number] (default: 0 15)
        exec-timeout [timeout time in minutes] (optional)
        transport input ssh
        login local
        exit
        ip ssh version 2
        ip ssh authentication-retries [number of retries] (default: 3)
        ip ssh time-out [number of seconds to timeout] (default: 60)
         */

        CommandUtil.doSshSecretOrPasswordConfig(in, appendlineList);
        CommandUtil.doSshDomainNameConfig(in, appendlineList);
        CommandUtil.doCryptoKeyBitsConfig(in, appendlineList);
        CommandUtil.doLineVtyConfig(in, appendlineList);
        CommandUtil.doExecTimeoutConfig(in, appendlineList);
        appendlineList.add("transport input ssh");
        appendlineList.add("login local");
        appendlineList.add("exit");
        appendlineList.add("ip ssh version 2");
        CommandUtil.doAuthRetriesConfig(in, appendlineList);
        CommandUtil.doSshTimeOutConfig(in, appendlineList);

    }

}
