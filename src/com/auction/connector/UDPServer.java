//package com.liukai.app;
//
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.InputStream;
//import java.io.OutputStream;
//import java.net.DatagramPacket;
//import java.net.DatagramSocket;
//import java.net.InetAddress;
//import java.net.Socket;
//import java.net.SocketException;
//
//import android.os.Bundle;
//import android.os.Message;
//
//public class UDPServer extends Thread {
//
//	DatagramPacket data = null;
//
//	public UDPServer(DatagramPacket data) {
//
//		this.data = data;
//	}
//
//	public static MessageActivity message = null;
//
//	public void run() {
//		// 12312312312312
//		String s = new String(data.getData()).trim();
//		// lisi,123123123.amr,164654654564564
//		try {
//			Socket socket = new Socket("10.3.128.169", 5555);
//			InputStream in = socket.getInputStream();
//			OutputStream out = socket.getOutputStream();
//			out.write(("get," + s.split(",")[1]).getBytes());
//			out.flush();
//			byte[] b = new byte[1000];
//			in.read(b);
//			long size = Long.parseLong(new String(b).trim().split(",")[1]);
//			out.write("ok".getBytes());
//			out.flush();
//			FileOutputStream fout = new FileOutputStream(new File("/sdcard/", s.split(",")[1]));
//			int len = 0;
//			long length = 0;
//			byte[] b1 = new byte[1024];
//			while ((len = in.read(b1)) != -1) {
//				fout.write(b1, 0, len);
//				length += len;
//				if (length >= size) {
//					break;
//				}
//			}
//			fout.close();
//			socket.close();
//			DatagramSocket socket1 = new DatagramSocket();
//			byte[] bb=s.split(",")[2].getBytes();
//			DatagramPacket data = new DatagramPacket(bb, bb.length,InetAddress.getByName("10.3.128.169"),8888);
//			socket1.send(data);
//
//			Message msg = new Message();
//			Bundle b3 = new Bundle();
//			b3.putString("username", s.split(",")[0]);
//			b3.putString("filename", s.split(",")[1]);
//			msg.setData(b3);
//			message.han.sendMessage(msg);
//
//		} catch (Exception e) {
//			// TODO: handle exception
//		}
//
//	}
//
//	public static void openServer() throws Exception {
//
//		DatagramSocket socket = new DatagramSocket(7777);
//		while (true) {
//			byte[] b = new byte[1000];
//			DatagramPacket data = new DatagramPacket(b, b.length);
//			socket.receive(data);
//			new UDPServer(data).start();
//		}
//	}
//
//}
