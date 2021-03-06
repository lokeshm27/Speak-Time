import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

public class Operations {

	private final int PORT = 8081;
	private final File flag = new File("C:\\ProgramData\\SpeakTime\\Temp\\Running.dat");
	private TrayIcon trayIcon;
	private ServerSocket server;
	private Socket socket;
	private final File dat = new File("C:\\ProgramData\\SpeakTime\\Data.dat");
	private boolean isOpen = false, isOption = false, isAbout = false, isDisable = false, isExit = false;
	private JFrame optionFrame, aboutFrame, openFrame, exitFrame, disableFrame;
	private BufferedReader br;
	private PrintWriter pw;
	private PopupMenu popup;
	private MenuItem disable;
	private MenuItem enable;
	private boolean enabled = true;
	Data data = new Data();
	speakThread speak = new speakThread(data);

	public void initTray() {
		try {
			if (!SystemTray.isSupported()) {
				return;
			}
			popup = new PopupMenu();
			SystemTray tray = SystemTray.getSystemTray();
			Image bi = Toolkit.getDefaultToolkit().getImage("res/sm.png");

			ActionListener exitListener = new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					exit();
				}
			};

			ActionListener defaultListener = new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					System.out.println("Action performed : default");
					open();
				}
			};

			ActionListener optionsListener = new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					System.out.println("Action performed : options");
					options();
				}
			};

			ActionListener syncListener = new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					System.out.println("Action performed : sync");
					sync();
				}
			};

			ActionListener aboutListener = new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					System.out.println("Action performed : About");
					about();
				}
			};

			ActionListener openListener = new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					System.out.println("Action performed : open");
					open();
				}
			};

			ActionListener testListener = new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					System.out.println("Action performed : Test");
					test();
				}
			};

			ActionListener disableListener = new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					System.out.println("Action performed : Disable");
					disable();
				}
			};

			ActionListener enableListener = new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					System.out.println("Action performed : enable");
					enable();
				}
			};

			MenuItem Exit = new MenuItem("Exit");
			Exit.addActionListener(exitListener);

			MenuItem options = new MenuItem("Options");
			options.addActionListener(optionsListener);

			MenuItem sync = new MenuItem("Sync Time");
			sync.addActionListener(syncListener);

			MenuItem about = new MenuItem("About Speak-Time");
			about.addActionListener(aboutListener);

			MenuItem open = new MenuItem("Open Speak-time");
			open.addActionListener(openListener);

			MenuItem test = new MenuItem("Test Audio now");
			test.addActionListener(testListener);

			disable = new MenuItem("Disable Speak-Time temporarily");
			disable.addActionListener(disableListener);

			enable = new MenuItem("Enable now");
			enable.addActionListener(enableListener);

			popup.add(open);
			popup.add(options);
			popup.addSeparator();

			popup.add(test);
			popup.add(sync);
			popup.add(disable);
			popup.addSeparator();

			popup.add(about);
			popup.add(Exit);

			trayIcon = new TrayIcon(bi, "Speak-Time", popup);
			trayIcon.setImageAutoSize(true);

			trayIcon.addActionListener(defaultListener);
			tray.add(trayIcon);
			trayIcon.displayMessage("Speak-Time", "Speak-Time is running.", TrayIcon.MessageType.INFO);
		} catch (Exception e) {
			System.err.println(e);
		}
	}

	public void showTrayMsg(String msg, String title, TrayIcon.MessageType type) {
		if ((msg != null) && (title != null)) {
			trayIcon.displayMessage(msg, title, type);
		}
	}

	@SuppressWarnings("resource")
	private void serial(Data ip) {
		try {
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(dat));
			oos.writeObject(ip);
			speak.setData(data);
		} catch (Exception e) {

		}
	}

	@SuppressWarnings("resource")
	private Data deserial() {
		Data temp = new Data();
		try {
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(dat));
			temp = (Data) ois.readObject();
		} catch (Exception e) {

		}
		return temp;
	}

	public void init() {
		if (!dat.exists()) {
			serial(new Data());
		}
		data = deserial();
		speak.setData(data);
	}

	public void disable() {
		//// TODO
	}

	public void enable() {
		//// TODO
	}

	public void open() {
		//// TODO
	}

	public void test() {
		if(enabled){
			speak.test();
		}else{
			showTrayMsg("Can not test audio when Speak-Time is disabled.", "Speak-Time", TrayIcon.MessageType.ERROR);
		}
	}

	public void sync() {
		speak.interrupt();
		speak = new speakThread(data);
		speak.start();
	}

	public void options() {
		if (isOption) {
			optionFrame.toFront();
			return;
		}
		optionFrame = new JFrame("Speak-Time | Options");
		optionFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		optionFrame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent we) {
				int res = JOptionPane.showConfirmDialog(null, "Are you sure to exit?\nChanges may not be saved.!",
						"Confirm Exit.", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
				if (res == JOptionPane.YES_OPTION) {
					optionFrame.dispose();
					isOption = false;
				}
			}
		});

		JLabel label1 = new JLabel("Speak time every");
		SpinnerNumberModel spinner = new SpinnerNumberModel(10, 5, 55, 5);
		String options[] = { "Minutes", "Hours" };
		JComboBox<Object> combo = new JComboBox<Object>(options);
		if (data.minutes >= 60) {
			spinner.setValue(data.minutes / 60);
			spinner.setMaximum(5);
			spinner.setStepSize(1);
			spinner.setMinimum(1);
			combo.setSelectedIndex(1);
		} else {
			spinner.setMinimum(5);
			spinner.setStepSize(5);
			spinner.setValue(data.minutes);
			spinner.setMaximum(55);
			combo.setSelectedIndex(0);
		}
		combo.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				int sel = combo.getSelectedIndex();
				if (sel == 0) {
					spinner.setMinimum(5);
					spinner.setStepSize(5);
					if (data.minutes < 60) {
						spinner.setValue(data.minutes);
					} else {
						spinner.setValue(10);
					}
					spinner.setMaximum(59);
				} else {
					spinner.setStepSize(1);
					if (data.minutes >= 60) {
						spinner.setValue(data.minutes / 60);
					} else {
						spinner.setValue(1);
					}
					spinner.setMaximum(5);
					spinner.setMinimum(1);
				}
			}
		});

		JCheckBox cb1 = new JCheckBox("Start Speak-Time on start-up.");
		JCheckBox cb2 = new JCheckBox("Don't speak time if audio is playing.");
		JCheckBox cb3 = new JCheckBox("ALways confirm before exiting.");
		cb1.setSelected(data.autoStart);
		cb2.setSelected(data.ignore);
		cb3.setSelected(data.confirm);

		JButton save = new JButton("Save");
		save.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				int res = JOptionPane.showConfirmDialog(null, "Are you sure to save changes.?", "Confirm Save",
						JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
				if (res == JOptionPane.YES_OPTION) {
					int mins;
					Data temp;

					if (combo.getSelectedIndex() == 0) {
						mins = (Integer) spinner.getValue();
						if ((mins % 5 == 0) && (mins >= 1) && (mins <= 55)) {
							temp = new Data(mins, cb1.isSelected(), cb2.isSelected(), cb3.isSelected());
							data = temp;
							sync();
							serial(temp);
							isOption = false;
							optionFrame.dispose();
						} else {
							JOptionPane.showMessageDialog(null, "Invlaid number for minutes.\nPlease try again.",
									"Error - Speak-Time", JOptionPane.ERROR_MESSAGE);
						}
					} else {
						mins = (Integer) spinner.getValue();
						if ((mins <= 5) && (mins >= 1)) {
							temp = new Data((mins * 60), cb1.isSelected(), cb2.isSelected(), cb3.isSelected());
							data = temp;
							sync();
							serial(temp);
							isOption = false;
							optionFrame.dispose();
						} else {
							JOptionPane.showMessageDialog(null, "Invlaid number for hours.\nPlease try again.",
									"Error - Speak-Time", JOptionPane.ERROR_MESSAGE);
						}
					}
				}
			}
		});

		JButton cancel = new JButton("Cancel");
		cancel.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				int res = JOptionPane.showConfirmDialog(null, "Are you sure to cancel?\nChanges may not be saved.!",
						"Confirm Exit", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
				if (res == JOptionPane.YES_OPTION) {
					isOption = false;
					optionFrame.dispose();
				}
			}
		});

		JPanel p1 = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(10, 10, 10, 10);
		gbc.insets = new Insets(5, 5, 5, 5);
		gbc.gridx = GridBagConstraints.RELATIVE;
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.NORTH;

		p1.add(label1, gbc);
		p1.add(new JSpinner(spinner), gbc);
		p1.add(combo, gbc);

		JPanel p2 = new JPanel(new GridBagLayout());
		GridBagConstraints g2 = new GridBagConstraints();
		g2.insets = new Insets(5, 5, 5, 5);
		g2.gridx = 0;
		g2.gridy = GridBagConstraints.RELATIVE;
		g2.anchor = GridBagConstraints.WEST;

		p2.add(cb1, g2);
		p2.add(cb2, g2);
		p2.add(cb3, g2);

		JPanel p3 = new JPanel(new GridBagLayout());
		GridBagConstraints g3 = new GridBagConstraints();
		g3.insets = new Insets(5, 10, 5, 10);

		g3.gridx = 0;
		g3.gridy = 0;
		p3.add(save, g3);

		g3.gridx = 1;
		g3.gridy = 0;
		p3.add(cancel, g3);

		JPanel p4 = new JPanel(new GridBagLayout());
		GridBagConstraints g4 = new GridBagConstraints();
		g4.insets = new Insets(5, 5, 5, 5);

		g4.gridx = 0;
		g4.gridy = 0;
		p4.add(p1, g4);

		g4.gridx = 0;
		g4.gridy = 1;
		p4.add(p2, g4);

		g4.gridx = 0;
		g4.gridy = 2;
		p4.add(p3, g4);

		optionFrame.add(p4, BorderLayout.CENTER);
		optionFrame.setSize(300, 400);
		optionFrame.setResizable(true);
		optionFrame.setLocationRelativeTo(null);
		isOption = true;
		optionFrame.setVisible(true);
	}

	public void exit() {
		if (!data.confirm) {
			try {
				server.close();
				flag.delete();
				System.exit(0);
			} catch (Exception e) {

			}
		} else {
			if (isExit) {
				exitFrame.toFront();
				return;
			}
			exitFrame = new JFrame("Confirm exit.!");
			exitFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

			JLabel label1 = new JLabel("Are you sure to exit Speak-Time.?");

			JCheckBox cb1 = new JCheckBox("Always confirm before exiting");
			cb1.setSelected(data.confirm);
			cb1.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					data = new Data(data.minutes, data.autoStart, data.ignore, cb1.isSelected());
					serial(data);
				}
			});

			JPanel p1 = new JPanel(new GridBagLayout());
			GridBagConstraints g1 = new GridBagConstraints();
			g1.insets = new Insets(5, 5, 5, 5);
			g1.gridx = 0;
			g1.gridy = GridBagConstraints.RELATIVE;
			g1.anchor = GridBagConstraints.WEST;

			p1.add(label1, g1);
			p1.add(cb1, g1);

			JButton yes = new JButton("Yes");
			yes.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					try {
						exitFrame.dispose();
						isExit = false;
						server.close();
						flag.delete();
						System.exit(0);
					} catch (Exception e) {

					}
				}
			});

			JButton no = new JButton("No");
			no.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					exitFrame.dispose();
					isExit = false;
				}
			});

			JPanel p2 = new JPanel(new GridBagLayout());
			GridBagConstraints g2 = new GridBagConstraints();
			g2.insets = new Insets(5, 15, 25, 15);

			g2.gridx = 0;
			g2.gridy = 0;
			p2.add(yes, g2);

			g2.gridx = 1;
			g2.gridy = 0;
			p2.add(no, g2);

			exitFrame.add(p1, BorderLayout.NORTH);
			exitFrame.add(p2, BorderLayout.SOUTH);
			exitFrame.setSize(300, 175);
			exitFrame.setLocationRelativeTo(null);
			exitFrame.setVisible(true);
		}
	}

	public void about() {
		//// TODO
	}

	@SuppressWarnings("resource")
	public boolean isWorking() {
		try {
			File Dir = new File("C:\\ProgramData\\SpeakTime");
			if (!(Dir.exists() && Dir.isDirectory())) {
				System.out.println("Creating Dir");
				File temp = new File("C:\\ProgramData\\SpeakTime\\Temp");
				Dir.mkdir();
				temp.mkdir();
				flag.createNewFile();
				return false;
			}

			if (flag.exists()) {
				try {
					InetAddress localhost = InetAddress.getLocalHost();
					Socket socket = new Socket(localhost, PORT);
					br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
					pw = new PrintWriter(socket.getOutputStream(), true);
					pw.println("Confirm");
					pw.flush();
					String inputLine;
					if ((inputLine = br.readLine()) != null) {
						if (inputLine.equals("Affirmative")) {
							System.out.println("Server running confirmed.");
							socket.close();
							return true;
						}
					}
				} catch (Exception e) {
					return false;
				}
				return true;
			} else {
				flag.createNewFile();
				return false;
			}

		} catch (Exception e) {

		}
		return false;
	}

	public void startServer() {
		try {
			server = new ServerSocket(PORT);
			Thread thread = new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						while (true) {
							new IPCThread(server.accept()).start();
						}
					} catch (Exception e) {

					}
				}
			});
			thread.setDaemon(true);
			thread.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void passMessage(String msg) {
		try {
			InetAddress localhost = InetAddress.getLocalHost();
			socket = new Socket(localhost, PORT);
			PrintWriter pw = new PrintWriter(socket.getOutputStream(), true);
			pw.println(msg);
			pw.flush();
			socket.close();
		} catch (Exception e) {

		}
	}

	public void onClose() {
		try {
			flag.delete();
			server.close();
			System.exit(0);
		} catch (Exception e) {

		}
	}

	public void startOperation(){
		speak.start();
	}
	
}
