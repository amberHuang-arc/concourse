/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication1;

import java.util.List;
import java.util.Scanner;

/**
 *
 */
public class CommandUtil {

    //ip ssh time-out [number of seconds to timeout] (default: 60)
    static void doSshTimeOutConfig(Scanner in, List<String> appendlineList) {
        System.out.println("ip ssh time-out in seconds (default 60): ");
        String timeoutSecNo = in.nextLine();
        if (timeoutSecNo.isEmpty()) {  // no input, then use default
            timeoutSecNo = "60";
        }
        System.out.println("---ip ssh timout in sec: " + timeoutSecNo);

        appendlineList.add("ip ssh time-out " + timeoutSecNo);
    }

    //ip ssh authentication-retries [number of retries] (default: 3)
    static void doAuthRetriesConfig(Scanner in, List<String> appendlineList) {
        System.out.println("ip ssh authentication-retries number(default 3): ");
        String retryNo = in.nextLine();
        if (retryNo.isEmpty()) {  // no input, then use default
            retryNo = "3";
        }
        System.out.println("---ip ssh authentication-retries number: " + retryNo);

        appendlineList.add("ip ssh authentication-retries " + retryNo);
    }

    //line vty [first number] [last number] (default: 0 15)
    static void doLineVtyConfig(Scanner in, List<String> appendlineList) {
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

    //exec-timeout [timeout time in minutes] (optional)
    static void doExecTimeoutConfig(Scanner in, List<String> appendlineList) {
        System.out.println("exec-timeout in minutes (optional): if no, just enter");
        String timeoutMin = in.nextLine();
        if (!timeoutMin.isEmpty()) {  // with input
            System.out.println("---exec-timeout in minutes: " + timeoutMin);
            appendlineList.add("exec-timeout " + timeoutMin);
        }
    }

    //banner motd x[message]x (optional)
    static void doBannerConfig(Scanner in, List<String> appendlineList) {
        System.out.println("need banner? if no, just enter. if yes, input banner message");
        String bannerStr = in.nextLine();
        if (!bannerStr.isEmpty()) {
            System.out.println("---banner: " + bannerStr);
            appendlineList.add("banner motd X" + bannerStr + "X");
        }
    }

    //enable password [password] (optional)
    static void doEnablePswConfig(Scanner in, List<String> appendlineList) {
        System.out.println("enable password? if yes, input password, if no, hit enter");
        String passwordStr = in.nextLine();
        if (!passwordStr.isEmpty()) {
            System.out.println("---password: " + passwordStr);
            appendlineList.add("enable password " + passwordStr);
        }
    }

    //enable secret [secret] (optional)
    static void doEnableSecretConfig(Scanner in, List<String> appendlineList) {
        System.out.println("enable secret? if yes, input secret, if no, hit enter");
        String secretStr = in.nextLine();
        if (!secretStr.isEmpty()) {
            System.out.println("---secret: " + secretStr);
            appendlineList.add("enable secret " + secretStr);
        }
    }

    //ip default-gateway [ip address of connected router] (required)
    static void doDefaultGatewayConfig(Scanner in, List<String> appendlineList) {
        //require default-gateway
        String defaultGatewayStr = null;
        do {
            System.out.println("input default gateway ip: ");
            defaultGatewayStr = in.nextLine();
        } while (defaultGatewayStr.isEmpty());
        System.out.println("---default gateway:" + defaultGatewayStr);
        appendlineList.add("ip default-gateway " + defaultGatewayStr);
    }

    //ip name-server [ip address of dns server]
    //ip domain lookup
    static void doDnsServerConfig(Scanner in, List<String> appendlineList) {
        System.out.println("need config DNS server? if no, just enter. if yes, input the dns server ip");
        String dnsIpStr = in.nextLine();
        if (!dnsIpStr.isEmpty()) {
            System.out.println("---DNS server ip: " + dnsIpStr);
            appendlineList.add("ip name-server " + dnsIpStr);
            appendlineList.add("ip domain lookup");
        }
    }

    //interface [interface] (required)
    static void doInterfaceConfig(Scanner in, List<String> appendlineList) {
        String intStr = "";
        do {
            System.out.println("input interface: ");
            intStr = in.nextLine();
        } while (intStr.isEmpty());
        System.out.println("---interface:" + intStr);
        appendlineList.add("interface " + intStr);
    }

    //switchport port-security maximum [amount] (default: 3)
    static void doPortSecMaxConfig(Scanner in, List<String> appendlineList) {
        System.out.println("input port-security max amount(default: 3):");
        String inputStr = in.nextLine();
        String maxAmtStr = (inputStr.isEmpty() ? "3" : inputStr);
        System.out.println("---port-security max amount: " + maxAmtStr);
        appendlineList.add("switchport port-security maximum " + maxAmtStr);
    }

    //switchport port-security violation [protect/restrict/shutdown] (default: restrict)
    static void doPortSecViolationConfig(Scanner in, List<String> appendlineList) {
        System.out.println("input port-sec violation code(only input number, default 2): 1.protect/2.restrict/3.shutdown");
        String violationNoStr = in.nextLine();
        if (violationNoStr.isEmpty()) {
            violationNoStr = "restrict";
        } else {
            int vioCodeInt = Integer.parseInt(violationNoStr.trim());
            violationNoStr = (vioCodeInt == 1 ? "protect" : (vioCodeInt == 3 ? "shutdown" : "restrict"));
        }
        System.out.println("---port-sec violation: " + violationNoStr);
        appendlineList.add("switchport port-security violation " + violationNoStr);
    }

    //switchport access vlan [vlan number] (required)
    static void doAccessVlanConfig(Scanner in, List<String> appendlineList) {
        String vlanNoStr = "";
        do {
            System.out.println("input access vlan number:");
            vlanNoStr = in.nextLine();
        } while (vlanNoStr.isEmpty());

        System.out.println("---access vlan no: " + vlanNoStr);
        appendlineList.add("switchport access vlan " + vlanNoStr);
    }

    //switchport trunk allowed vlan [vlan number] (required)
    static void doTrunkAllowedVlanConfig(Scanner in, List<String> appendlineList) {
        String vlanNoStr = "";
        do {
            System.out.println("input trunk allowed vlan number:");
            vlanNoStr = in.nextLine();
        } while (vlanNoStr.isEmpty());
        System.out.println("---trunk allowed vlan no: " + vlanNoStr);
        appendlineList.add("switchport trunk allowed vlan " + vlanNoStr);
    }

    //switchport trunk native vlan [native vlan number](optional)
    static void doTrunkNativeVlanConfig(Scanner in, List<String> appendlineList) {
        System.out.println("input native vlan number(optional): if no, just enter");
        String nativeVlanNoStr = in.nextLine();
        if (!nativeVlanNoStr.isEmpty()) {
            System.out.println("---native vlan no:" + nativeVlanNoStr);
            appendlineList.add("switchport trunk native vlan " + nativeVlanNoStr);
        }
    }

    //name [vlan name]
    static void doVlanNameConfig(Scanner in, List<String> appendlineList) {
        System.out.println("input vlan name: ");
        String vlanNameStr = in.nextLine();
        System.out.println("---vlan name:" + vlanNameStr);
        appendlineList.add("name " + vlanNameStr);
    }

    //ip address [ip address] [subnet mask] (optional)
    static void doVlanIpConfig(Scanner in, List<String> appendlineList) {
        System.out.println("ip address? if no, just enter ");
        String ipStr = in.nextLine();
        if (!ipStr.isEmpty()) {
            System.out.println("---interface vlan ip:" + ipStr);

            System.out.println("input subnet mask: ");
            String submaskStr = in.nextLine();
            System.out.println("---subnet mask:" + submaskStr);

            appendlineList.add("ip address " + ipStr + " " + submaskStr);
        }
    }

    //line console [console number] (default: 0)
    static void doLineConsoleConfig(Scanner in, List<String> appendlineList) {
        System.out.println("input line console number(default 0): ");
        String consoleNoStr = in.nextLine();
        if (consoleNoStr.isEmpty()) {  // no input, then use default
            consoleNoStr = "0";
        }
        System.out.println("---line console number: " + consoleNoStr);
        appendlineList.add("line console " + consoleNoStr);
    }

    //password [password] (required)
    static void doPasswordConfig(Scanner in, List<String> appendlineList) {
        String password = "";
        do {
            System.out.println("input password: ");
            password = in.nextLine();
        } while (password.isEmpty());
        System.out.println("---password: " + password);
        appendlineList.add("password " + password);
    }

    //username [username] secret [secret]
    //(alternative) username [username] password [password]
    static void doSshSecretOrPasswordConfig(Scanner in, List<String> appendlineList) {
        String username = "";
        do {
            System.out.println("input username: ");
            username = in.nextLine();
        } while (username.isEmpty());
        System.out.println("---username: " + username);

        String keyword = "";
        while (true) {
            System.out.println("Config Secret or Password? 1.secret, 2.password ");
            String input = in.nextLine();
            if ("1".equals(input)) {
                keyword = "secret";
                break;
            }
            if ("2".equals(input)) {
                keyword = "password";
                break;
            }
        }

        String secretOrPsw = getSecretOrPassword(keyword, in);
        appendlineList.add("username " + username + " " + keyword + " " + secretOrPsw);
    }

    private static String getSecretOrPassword(String keyword, Scanner in) {
        String secretOrPsw = "";
        do {
            System.out.println("input " + keyword + ": ");
            secretOrPsw = in.nextLine();
        } while (secretOrPsw.isEmpty());
        System.out.println("---" + keyword + ": " + secretOrPsw);

        return secretOrPsw;
    }

    //ip domain-name [domain name] (required)
    static void doSshDomainNameConfig(Scanner in, List<String> appendlineList) {
        String domainName = "";
        do {
            System.out.println("input domainName: ");
            domainName = in.nextLine();
        } while (domainName.isEmpty());
        System.out.println("---domainName: " + domainName);
        appendlineList.add("ip domain-name " + domainName);
    }

    static void doCryptoKeyBitsConfig(Scanner in, List<String> appendlineList) {
        System.out.println("crypto key generate rsa [bits] (default: 1024):");
        String keybits = in.nextLine();
        if (keybits.isEmpty()) {  // no input, then use default
            keybits = "1024";
        }
        System.out.println("---crypto key generate rsa [bits]: " + keybits);
        appendlineList.add("crypto key generate rsa " + keybits);
    }

}
