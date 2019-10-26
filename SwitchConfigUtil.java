/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication1;

import java.util.List;
import java.util.Map;
import java.util.Scanner;
import static javaapplication1.ConfigUtil.ROOT_FROM_FILE_PATH;
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
        String fromFile = ROOT_FROM_FILE_PATH + "switch.conf";
        String toFile = ROOT_TO_FILE_PATH + toFileName;

        //build the replacement keywords map, then process replacement
        ConfigFilesGenerator helper = new ConfigFilesGenerator(fromFile, toFile);
        Map<String, String> keywordsReplaceMap = helper.getKeywordsReplaceMap();
        keywordsReplaceMap.put("$name$", hostNameStr);

        List<String> appendlineList = helper.getAppendLinesList();

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
        System.out.println("need banner? ");
        String bannerStr = in.nextLine();
        if (!bannerStr.isEmpty()) {
            System.out.println("---banner: " + bannerStr);
            appendlineList.add("banner motd X" + bannerStr + "X");
        }

        System.out.println("enable password? if yes, input password, if no, hit enter");
        String passwordStr = in.nextLine();
        if (!passwordStr.isEmpty()) {
            System.out.println("---password: " + passwordStr);
            appendlineList.add("enable password " + passwordStr);
        }

        System.out.println("enable secret? if yes, input secret, if no, hit enter");
        String secretStr = in.nextLine();
        if (!secretStr.isEmpty()) {
            System.out.println("---secret: " + secretStr);
            appendlineList.add("enable secret " + secretStr);
        }

        appendlineList.add("service password-encryption");

        //require default-gateway
        String defaultGatewayStr = null;
        do {
            System.out.println("input default gateway ip: ");
            defaultGatewayStr = in.nextLine();
        } while (defaultGatewayStr.isEmpty());
        System.out.println("---default gateway:" + defaultGatewayStr);
        appendlineList.add("ip default-gateway " + defaultGatewayStr);

        System.out.println("need config DNS server? if no, just enter. if yes, input the dns server ip");
        String dnsIpStr = in.nextLine();
        if (!dnsIpStr.isEmpty()) {
            System.out.println("---DNS server ip: " + dnsIpStr);
            String addedLine = "ip name-server " + dnsIpStr;
            appendlineList.add(addedLine);
            appendlineList.add("ip domain lookup");
        }
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
        //required interface
        String intStr = "";
        do {
            System.out.println("input interface: ");
            intStr = in.nextLine();
        } while (intStr.isEmpty());
        System.out.println("---interface:" + intStr);
        appendlineList.add("interface " + intStr);

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

        System.out.println("input port-security max amount(default: 3):");
        String inputStr = in.nextLine();
        String maxAmtStr = (inputStr.isEmpty() ? "3" : inputStr);
        System.out.println("---port-security max amount: " + maxAmtStr);

        System.out.println("input port-sec violation code(only input number, default 2): 1.protect/2.restrict/3.shutdown");
        String violationNoStr = in.nextLine();
        if (violationNoStr.isEmpty()) {
            violationNoStr = "restrict";
        } else {
            int vioCodeInt = Integer.parseInt(violationNoStr.trim());
            violationNoStr = (vioCodeInt == 1 ? "protect" : (vioCodeInt == 3 ? "shutdown" : "restrict"));
        }
        System.out.println("---port-sec violation: " + violationNoStr);

        appendlineList.add("switchport port-security");
        appendlineList.add("switchport port-security maximum " + maxAmtStr);
        appendlineList.add("switchport port-security mac-address sticky");
        appendlineList.add("switchport port-security violation " + violationNoStr);

    }

    //Step 3 - Part A
    private static void configAccess(Scanner in, List<String> appendlineList) {
        //switchport mode access
        //switchport access vlan [vlan number]

        String vlanNoStr = "";
        do {
            System.out.println("input access vlan number:");
            vlanNoStr = in.nextLine();
        } while (vlanNoStr.isEmpty());

        System.out.println("---access vlan no: " + vlanNoStr);

        appendlineList.add("switchport mode access");
        appendlineList.add("switchport access vlan " + vlanNoStr);

    }

    //Step 3 - Part T
    private static void configTrunk(Scanner in, List<String> appendlineList) {
        String vlanNoStr = "";
        do {
            System.out.println("input trunk allowed vlan number:");
            vlanNoStr = in.nextLine();
        } while (vlanNoStr.isEmpty());
        System.out.println("---trunk allowed vlan no: " + vlanNoStr);

        System.out.println("input native vlan number(optional): if no, just enter");
        String nativeVlanNoStr = in.nextLine();
        if (!nativeVlanNoStr.isEmpty()) {
            System.out.println("---native vlan no:" + nativeVlanNoStr);
        }

        //switchport mode trunk
        //switchport nonegotiate
        //switchport trunk allowed vlan [vlan number]
        //switchport trunk native vlan [native vlan number]
        appendlineList.add("switchport mode trunk");
        appendlineList.add("switchport nonegotiate");
        appendlineList.add("switchport trunk allowed vlan " + vlanNoStr);
        if (!nativeVlanNoStr.isEmpty()) {
            appendlineList.add("switchport trunk native vlan " + nativeVlanNoStr);
        }

    }

    private static void configVlans(Scanner in, List<String> appendlineList) {
        while (true) {
            System.out.println("config Vlan? if yes, input vlan number, if no, just press enter");
            String vlanNo = in.nextLine();
            if (vlanNo.isEmpty()) {
                System.out.println("---No more Vlan Config");
                break;
            } else {
                System.out.println("---Vlan number:" + vlanNo);
                appendlineList.add("vlan " + vlanNo);

                System.out.println("input vlan name: ");
                String vlanNameStr = in.nextLine();
                System.out.println("---vlan name:" + vlanNameStr);
                appendlineList.add("name " + vlanNameStr);
                appendlineList.add("exit");

                configIntVlan(vlanNo, in, appendlineList);
            }
        }
    }

    private static void configIntVlan(String vlanNo, Scanner in, List<String> appendlineList) {
        //config interface vlan
        appendlineList.add("interface vlan " + vlanNo);

        //Step 2 - Part 2
        System.out.println("ip? if no, just enter ");
        String ipStr = in.nextLine();
        if (!ipStr.isEmpty()) {
            System.out.println("---interface vlan ip:" + ipStr);

            System.out.println("input subnet mask: ");
            String submaskStr = in.nextLine();
            System.out.println("---subnet mask:" + submaskStr);

            appendlineList.add("ip address " + ipStr + " " + submaskStr);
        }

        //Step 2 - Part 3
        appendlineList.add("no shutdown");
        appendlineList.add("exit");
    }

    //Step 4
    private static void configOptions(Scanner in, List<String> appendlineList) {
        System.out.println("console config? if no, just enter. if yes, hit any other key ");
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
        System.out.println("input line console number(default 0): ");
        String consoleNoStr = in.nextLine();
        if (consoleNoStr.isEmpty()) {  // no input, then use default
            consoleNoStr = "3";
        }
        System.out.println("---line console number: " + consoleNoStr);

        String password = "";
        do {
            System.out.println("input password: ");
            password = in.nextLine();
        } while (password.isEmpty());
        System.out.println("---password: " + password);

        //line console [console number] (default: 0)
        //password [password]
        //login
        //exit
        appendlineList.add("line console " + consoleNoStr);
        appendlineList.add("password " + password);
        appendlineList.add("login");
        appendlineList.add("exit");
    }

    //Step 4 - Part T
    private static void configTelnet(Scanner in, List<String> appendlineList) {
        //line vty [first number] [last number] (default: 0 15)
        //exec-timeout [timeout time in minutes] (optional)
        //password [password]
        //transport input telnet
        //login
        //exit

        doLineVtyConfig(in, appendlineList);

        doExecTimeoutConfig(in, appendlineList);

        String password = "";
        do {
            System.out.println("input password: ");
            password = in.nextLine();
        } while (password.isEmpty());
        System.out.println("---password: " + password);

        appendlineList.add("password " + password);
        appendlineList.add("transport input telnet");
        appendlineList.add("login");
        appendlineList.add("exit");
    }

    //Step 4 - Part S
    private static void configSsh(Scanner in, List<String> appendlineList) {
        /*
        username [username] secret [password]
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

        String username = "";
        do {
            System.out.println("input username: ");
            username = in.nextLine();
        } while (username.isEmpty());
        System.out.println("---username: " + username);

        String password = "";
        do {
            System.out.println("input password: ");
            password = in.nextLine();
        } while (password.isEmpty());
        System.out.println("---password: " + password);

        String domainName = "";
        do {
            System.out.println("input domainName: ");
            domainName = in.nextLine();
        } while (domainName.isEmpty());
        System.out.println("---domainName: " + password);

        System.out.println("crypto key generate rsa [bits] (default: 1024):");
        String keybits = in.nextLine();
        if (keybits.isEmpty()) {  // no input, then use default
            keybits = "1024";
        }
        System.out.println("---crypto key generate rsa [bits]: " + keybits);

        appendlineList.add("username " + username + " secret " + password);
        appendlineList.add("crypto key generate rsa " + keybits);
        doLineVtyConfig(in, appendlineList);
        doExecTimeoutConfig(in, appendlineList);
        appendlineList.add("transport input ssh");
        appendlineList.add("login local");
        appendlineList.add("exit");
        appendlineList.add("ip ssh version 2");
        doAuthRetriesConfig(in, appendlineList);
        doSshTimeOutConfig(in, appendlineList);

    }

    private static void doSshTimeOutConfig(Scanner in, List<String> appendlineList) {
        System.out.println("ip ssh time-out in seconds (default 60): ");
        String timeoutSecNo = in.nextLine();
        if (timeoutSecNo.isEmpty()) {  // no input, then use default
            timeoutSecNo = "60";
        }
        System.out.println("---ip ssh timout in sec: " + timeoutSecNo);

        appendlineList.add("ip ssh time-out " + timeoutSecNo);
    }

    private static void doAuthRetriesConfig(Scanner in, List<String> appendlineList) {
        System.out.println("ip ssh authentication-retries number(default 3): ");
        String retryNo = in.nextLine();
        if (retryNo.isEmpty()) {  // no input, then use default
            retryNo = "3";
        }
        System.out.println("---ip ssh authentication-retries number: " + retryNo);

        appendlineList.add("ip ssh authentication-retries " + retryNo);
    }

    private static void doLineVtyConfig(Scanner in, List<String> appendlineList) {
        System.out.println("input line vty first number(default 0): ");
        String firstNo = in.nextLine();
        if (firstNo.isEmpty()) {  // no input, then use default
            firstNo = "0";
        }
        System.out.println("---line vty first number: " + firstNo);

        System.out.println("input line vty second number(default 15): ");
        String secondNo = in.nextLine();
        if (secondNo.isEmpty()) {  // no input, then use default
            secondNo = "15";
        }
        System.out.println("---line vty second number: " + secondNo);

        appendlineList.add("line vty " + firstNo + " " + secondNo);
    }

    private static void doExecTimeoutConfig(Scanner in, List<String> appendlineList) {
        System.out.println("exec-timeout in minutes (optional): if no, just enter");
        String timeoutMin = in.nextLine();
        if (!timeoutMin.isEmpty()) {  // with input
            System.out.println("---exec-timeout in minutes: " + timeoutMin);
            appendlineList.add("exec-timeout " + timeoutMin);
        }
    }

}
