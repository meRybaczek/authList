import com.fazecast.jSerialComm.SerialPort;

import java.io.IOException;
import java.io.InputStream;

public class PortConnection {
    private static final int PORT_NO = 1;
    private final StringBuilder measurement;
    private final SerialPort comPort = SerialPort.getCommPorts()[PORT_NO];

    public PortConnection(StringBuilder measurements) {
        this.measurement = measurements;
    }

    public StringBuilder getMeasurement() {
        openPort();
        InputStream in = comPort.getInputStream();

        try {
            int bytesPerPacket = getBytesPerPacket();
            byte[] buffer = new byte[bytesPerPacket];

            // Czekaj, aż dane będą dostępne
            while (in.available() < bytesPerPacket) {
                Thread.sleep(100);
            }

            // Odczytaj dane do bufora
            in.read(buffer, 0, bytesPerPacket);

            // Konwertuj bajty do Stringa
            measurement.append(new String(buffer));
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        } finally {
            comPort.flushIOBuffers();
            comPort.closePort();
        }

        return measurement;
    }

    private void openPort() {
        comPort.openPort();
        comPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 0, 0);
        comPort.flushIOBuffers();
    }

    private int getBytesPerPacket() throws InterruptedException {
        Thread.sleep(3000);
        return comPort.bytesAvailable();
    }

    //dla testow
    public static void main(String[] args) {

        for (int i = 0; i < 20; i++) {
            StringBuilder stringBuilder = new StringBuilder();
            PortConnection connection = new PortConnection(stringBuilder);
            StringBuilder measurement = connection.getMeasurement();
            System.out.println(measurement);
        }

    }
}
