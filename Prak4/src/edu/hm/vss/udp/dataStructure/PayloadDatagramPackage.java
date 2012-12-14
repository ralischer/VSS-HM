package edu.hm.vss.udp.dataStructure;

import java.net.DatagramPacket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*
 * Creates a DatagramPacket with multiple Data fields.
 * The amount and length of the datafields is encoded in the datagram payload 
 * with the method createDataGramPacket. It can be decoded with the method
 * getDatagramContent
 */
public class PayloadDatagramPackage {

	private PayloadDatagramPackage() {
		throw new RuntimeException();
	}

	/**
	 * Creates a DatagramPacket encoding the different data portions into the
	 * payload
	 * 
	 * @param data
	 *            Data to encode inside the DatagramPacket
	 * @param totalLength
	 *            Length of the payload
	 * @return
	 */
	public static DatagramPacket createDatagramPacket(List<byte[]> data,
			int totalLength) {
		int payloads = data.size();
		byte[] result = new byte[totalLength];
		result[0] = (byte) payloads;
		int index = 1;
		for (int i = 0; i < payloads; i++) {
			byte[] nextBlock = data.get(i);
			if (nextBlock.length > 256) {
				throw new IllegalArgumentException(
						"each payload part mustn't exceed 256 byte");
			}
			result[index++] = (byte) nextBlock.length;
			for (int j = 0; j < nextBlock.length; j++) {
				result[index++] = nextBlock[j];
			}
		}
		return new DatagramPacket(result, totalLength);
	}

	/**
	 * Retreives the dataportions of a DatagramPacket previously encoded whith
	 * createDatagramPacket
	 * 
	 * @param data
	 *            containing the encoded payload
	 * @return
	 */
	public static ArrayList<byte[]> getDatagramContent(DatagramPacket data) {
		byte[] content = data.getData();
		int blocks = (int) content[0];
		ArrayList<byte[]> result = new ArrayList<byte[]>(blocks);
		int currentBlockIndex = 1;
		for (int i = 0; i < blocks; i++) {
			int nextBlockIndex = currentBlockIndex + content[currentBlockIndex]
					+ 1;
			result.add(Arrays.copyOfRange(content, currentBlockIndex + 1,
					nextBlockIndex));
			currentBlockIndex = nextBlockIndex;
		}
		return result;
	}

	public static void main(String[] args) {
		List<byte[]> data = new ArrayList<byte[]>(2);
		data.add("Hello".getBytes());
		data.add("World".getBytes());
		DatagramPacket packet = createDatagramPacket(data, 1024);
		ArrayList<byte[]> result = getDatagramContent(packet);
		System.out.println(new String(result.get(0)) + "\n"
				+ new String(result.get(1)));
	}
}
