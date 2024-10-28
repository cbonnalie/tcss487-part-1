import java.util.*;
import java.io.*;

public class Main {
    
    public static final HexFormat HEXF = HexFormat.of();
    
    private static final String[] SHA3_TEST_PATHS = {
        "tests/sha-3bytetestvectors/SHA3_224ShortMsg.rsp",
        "tests/sha-3bytetestvectors/SHA3_224LongMsg.rsp",
        "tests/sha-3bytetestvectors/SHA3_256ShortMsg.rsp",
        "tests/sha-3bytetestvectors/SHA3_256LongMsg.rsp",
        "tests/sha-3bytetestvectors/SHA3_384ShortMsg.rsp",
        "tests/sha-3bytetestvectors/SHA3_384LongMsg.rsp",
        "tests/sha-3bytetestvectors/SHA3_512LongMsg.rsp",
        "tests/sha-3bytetestvectors/SHA3_512LongMsg.rsp"
    };

    private static void sampleTest() {
        byte[] message = HEXF.parseHex("06");
        //byte[] message = HEXF.parseHex("01997b5853");
        //byte[] message = HEXF.parseHex("a7ffc6f8bf1ed76651c14756a061d662f580ff4de43b49fa82d80a4b80f8434aa7ffc6f8bf1ed76651c14756a061d662f580ff4de43b49fa82d80a4b80f8434a");
        //byte[] message = HEXF.parseHex("A3A3A3A3A3A3A3A3A3A3A3A3A3A3A3A3A3A3A3A3A3A3A3A3A3A3A3A3A3A3A3A3A3A3A3A3A3A3A3A3A3A3A3A3A3A3A3A3A3A3A3A3A3A3A3A3A3A3A3A3A3A3A3A3A3A3A3A3A3A3A3A3A3A3A3A3A3A3A3A3A3A3A3A3A3A3A3A3A3A3A3A3A3A3A3A3A3A3A3A3A3A3A3A3A3A3A3A3A3A3A3A3A3A3A3A3A3A3A3A3A3A3A3A3A3A3A3A3A3A3A3A3A3A3A3A3");
        byte[] result = SHA3SHAKE.SHA3(256, message, null);
        byte[] expected = HEXF.parseHex("a7ffc6f8bf1ed76651c14756a061d662f580ff4de43b49fa82d80a4b80f8434a");
    }

    private static List<TestResult> testFromFileSHA3(File file) {
        List<TestResult> results = new ArrayList<TestResult>();

        try {
            Scanner scanner = new Scanner(file);
            String line = scanner.nextLine();

            while (line.length() > 0 && line.charAt(0) != '[') {
                line = scanner.nextLine();
            }
            line = scanner.nextLine();

            int suffix = Integer.valueOf(line.substring(5, 8));

            scanner.nextLine();

            // loop
            while (scanner.hasNextLine()) {
                int messageLength = Integer.valueOf(scanner.nextLine().substring(6));
                byte[] message = HEXF.parseHex(scanner.nextLine().substring(6));
                byte[] result = SHA3SHAKE.SHA3(suffix, message, null);
                byte[] expected = HEXF.parseHex(scanner.nextLine().substring(5));
                String name = "SHA3-" + suffix + " L=" + messageLength;
                results.add(new TestResult(name, result, expected));

                scanner.nextLine();
            }
        } catch (FileNotFoundException e) {
            System.err.println("Could not find file to test.");
            e.printStackTrace();
        }

        return results;
    }

    private static void runAllTests(boolean additionalInfo) {
        int numPassed = 0;
        int totalTests = 0;

        for (String path : SHA3_TEST_PATHS) {
            File file = new File(path);
            for (TestResult result : testFromFileSHA3(file)) {
                if ( result.passed() ) { numPassed++; }

                totalTests++;
                if (additionalInfo) { System.out.println(result.toString()); }
            }
        }

        // TODO more test files and suffixes

        if ( numPassed == totalTests ) {
            System.out.println("Passed all tests.");
        } else {
            System.out.println("Passed " + numPassed + " of " + totalTests + " total tests.");
        }
    }

    public static void main(String[] args) {
        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "test":
                    //runAllTests(false);
                    sampleTest();
                    break;
                default: continue;
            }
        }
    }

}
