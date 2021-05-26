package main;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Image;
import java.awt.MouseInfo;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import element.tree.undoRedo.UndoRedoListener;
import menu.Botao;
import menu.Menu;
import menu.Toggle;
import element.Painel;
import element.tree.objeto.Objeto;
import element.tree.objeto.conexao.Conexao;
import element.tree.objeto.modulo.Modulo;
import element.tree.Cor;
import element.tree.ObjetoFocusListener;
import element.tree.Tree;
@SuppressWarnings({"serial","unchecked","static-access"})
public class TimeSort{
	public static void main(String[]args){new TimeSort(args);}
	public TimeSort(String[]args){
		try{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}catch(ClassNotFoundException|InstantiationException|IllegalAccessException|UnsupportedLookAndFeelException erro){
			Tree.mensagem("Erro ao configurar janela!\n"+erro,Tree.Options.ERRO);
		}
		fullscreen(false);
		getConfigIni();
		tree.setFocusOn(new Objeto[]{tree.getMestre()});
		janela.requestFocus();
		if(args.length>0)abrir(new File(args[0]));
	}
	private Tree tree;
	private File link=null;
	private Toggle fullscreen;
	private Toggle separateText;
	private Toggle autoFocusText;
	private Toggle showGrid;
	private Toggle lineWrap;
	private Toggle showAllChars;
	public static Cor MENU=new Cor(220,220,220);
	private Font fonte=new Font("Segoe UI Emoji",Font.PLAIN,12);
		public void setFonte(Font fonte){
			this.fonte=fonte;
			tree.setFonte(fonte);
		}
//	private Object[]entLimOpcoes={"Restrito",50,100,200,300,500,1000,"Sem restrição"};
	private Object[]doLimOpcoes={"Desativado",50,100,200,300,500,1000,"Sem restrição"};
	private String ini="configMindSort.ini";
	private Painel painel;
	private String titulo="MindSort";
	private JMenuBar menu;
//	private Popup popup;
//	private List<Icone>icones=new ArrayList<Icone>();
	private JFrame janela=new JFrame(){{
		setTitle(titulo);
		setSize(618,618);
		setMinimumSize(new Dimension(180,180));
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBackground(Tree.FUNDO);
		setLocationRelativeTo(null);
		setLayout(new BorderLayout());
		setIconImage(getImage("Icone"));
		setFont(fonte);
		setJMenuBar(menu=new JMenuBar(){{
			JMenuBar menu=this;
			final Cor corBorda=Cor.getChanged(Modulo.Cores.FUNDO,0.7f);
			setBorder(BorderFactory.createMatteBorder(0,0,1,0,corBorda));
			setPreferredSize(new Dimension(getWidth(),20));
			setBackground(TimeSort.MENU);
			setForeground(Tree.Fonte.DARK);
			final Font fonteMenu=new Font(fonte.getName(),Font.BOLD,fonte.getSize());
			UIManager.put("Menu.font",fonteMenu);
			UIManager.put("MenuItem.font",fonteMenu);
			UIManager.put("Menu.selectionBackground",Cor.getChanged(TimeSort.MENU,1.1f));
			UIManager.put("MenuItem.selectionBackground",Cor.getChanged(TimeSort.MENU,1.1f));
			UIManager.put("PopupMenu.border",BorderFactory.createLineBorder(corBorda));
			UIManager.put("Separator.foreground",corBorda);
			UIManager.getLookAndFeelDefaults().put("MenuItem.acceleratorForeground",corBorda);
			UIManager.getLookAndFeelDefaults().put("MenuItem.acceleratorFont",fonte);
//ARQUIVO
			add(new Menu(menu,"Arquivo"){{
	//NOVO
				add(new Botao(menu,"Novo",new AbstractAction(){public void actionPerformed(ActionEvent a){
						if(salvarAntes()){novo(choose("Novo"));abrir(link);}
					}}){{
					setIcon(new ImageIcon(getImage("Novo")));
					setAtalho(Event.CTRL_MASK,KeyEvent.VK_N,true);
				}});
	//ABRIR
				add(new Botao(menu,"Abrir...",new AbstractAction(){public void actionPerformed(ActionEvent a){if(salvarAntes())abrir(choose("Abrir"));}}){{
					setIcon(new ImageIcon(getImage("Abrir")));
					setAtalho(Event.CTRL_MASK,KeyEvent.VK_O,true);
				}});
	//SALVAR
				add(new Botao(menu,"Salvar",new AbstractAction(){public void actionPerformed(ActionEvent a){
						if(link==null)novo(choose("Salvar"));
						salvar(link);
					}}){{
					setIcon(new ImageIcon(getImage("Salvar")));
					setAtalho(Event.CTRL_MASK,KeyEvent.VK_S,true);
				}});
	//SALVAR COMO
				add(new Botao(menu,"Salvar Como...",new AbstractAction(){public void actionPerformed(ActionEvent a){novo(choose("Salvar Como"));}}){{
					setIcon(new ImageIcon(getImage("Salvar Como")));
					setAtalho(Event.SHIFT_MASK+Event.CTRL_MASK,KeyEvent.VK_S,true);
				}});
	//SAIR
				add(new Botao(menu,"Sair",new AbstractAction(){public void actionPerformed(ActionEvent a){if(salvarAntes())fechar();}}){{
					setIcon(new ImageIcon(getImage("Sair")));
					setAtalho(Event.CTRL_MASK,KeyEvent.VK_W,true);
				}});
			}});
//EDITAR
			add(new Menu(menu,"Editar"){{
	//DESFAZER
				add(new Botao(menu,"Desfazer",new AbstractAction(){
						public void actionPerformed(ActionEvent a){}
					}){{
					setIcon(new ImageIcon(getImage("Desfazer")));
					setAtalho(Event.CTRL_MASK,KeyEvent.VK_Z,true);
				}});
	//REFAZER
				add(new Botao(menu,"Refazer",new AbstractAction(){
						public void actionPerformed(ActionEvent a){}
					}){{
					setIcon(new ImageIcon(getImage("Refazer")));
					setAtalho(Event.CTRL_MASK,KeyEvent.VK_Y,true);
				}});
				add(new JSeparator());
	//OBJETO
				add(new Menu(menu,"Objeto"){{
		//MÓDULO
					add(new Menu(menu,"Módulo"){{
			//EDITAR TÍTULO
						add(new Botao(menu,"Editar Título",new AbstractAction(){
								public void actionPerformed(ActionEvent a){}
							}){{
							setIcon(new ImageIcon(getImage("Editar Título")));
							setAtalho(0,KeyEvent.VK_F2,true);
						}});
			//RELACIONAR
						add(new Botao(menu,"Relacionar",new AbstractAction(){
								public void actionPerformed(ActionEvent a){}
							}){{
							setIcon(new ImageIcon(getImage("Relacionar")));
							setAtalho(Event.CTRL_MASK,KeyEvent.VK_R,true);
						}});
					}});
		//CRIAR
					add(new Botao(menu,"Criar",new AbstractAction(){
							public void actionPerformed(ActionEvent a){}
						}){{
						setIcon(new ImageIcon(getImage("Criar")));
						setAtalho(Event.CTRL_MASK,KeyEvent.VK_G,true);
					}});
		//EXCLUIR
					add(new Botao(menu,"Excluir",new AbstractAction(){
							public void actionPerformed(ActionEvent a){}
						}){{
						setIcon(new ImageIcon(getImage("Excluir")));
						setAtalho(Event.CTRL_MASK,KeyEvent.VK_D,true);
					}});
				}});
				add(new JSeparator());
	//RECORTAR
				add(new Botao(menu,"Recortar",new AbstractAction(){
						public void actionPerformed(ActionEvent a){}
					}){{
					setIcon(new ImageIcon(getImage("Recortar")));
					setAtalho(Event.CTRL_MASK,KeyEvent.VK_X,true);
				}});
	//COPIAR
				add(new Botao(menu,"Copiar",new AbstractAction(){
						public void actionPerformed(ActionEvent a){}
					}){{
					setIcon(new ImageIcon(getImage("Copiar")));
					setAtalho(Event.CTRL_MASK,KeyEvent.VK_C,true);
				}});
	//COLAR
				add(new Botao(menu,"Colar",new AbstractAction(){
						public void actionPerformed(ActionEvent a){}
					}){{
					setIcon(new ImageIcon(getImage("Colar")));
					setAtalho(Event.CTRL_MASK,KeyEvent.VK_V,true);
				}});
	//EXCLUIR
				add(new Botao(menu,"Excluir",new AbstractAction(){
						public void actionPerformed(ActionEvent a){}
					}){{
					setIcon(new ImageIcon(getImage("Excluir")));
					setAtalho(0,KeyEvent.VK_DELETE,true);
				}});
				add(new JSeparator());
	//SELECIONAR TUDO
				add(new Botao(menu,"Selecionar Tudo",new AbstractAction(){
						public void actionPerformed(ActionEvent a){}
					}){{
					setIcon(new ImageIcon(getImage("Selecionar Tudo")));
					setAtalho(Event.CTRL_MASK,KeyEvent.VK_A,true);
				}});
	//DESELECIONAR TUDO
				add(new Botao(menu,"Deselecionar Tudo",new AbstractAction(){
						public void actionPerformed(ActionEvent a){}
					}){{
					setIcon(new ImageIcon(getImage("Deselecionar Tudo")));
					setAtalho(0,KeyEvent.VK_ESCAPE,true);
				}});
			}});
//EXIBIR
			add(new Menu(menu,"Exibir"){{
	//TELA CHEIA
				add(fullscreen=new Toggle(menu,"Tela Cheia",new Runnable(){public void run(){
						fullscreen(fullscreen.isPressed());
						separateText.setEnabled(!fullscreen.isPressed());
					}}){{
					setIcon(new ImageIcon(getImage("Tela Cheia")));
					setAtalho(0,KeyEvent.VK_F11,true);
				}});
	//ZOOM
				add(new Menu(menu,"Zoom"){{
		//AUMENTAR
					add(new Botao(menu,"Aumentar",new AbstractAction(){
							public void actionPerformed(ActionEvent a){}
						}){{
						setIcon(new ImageIcon(getImage("Aumentar")));
						setAtalho(Event.CTRL_MASK,KeyEvent.VK_EQUALS,true);
					}});
		//DIMINUIR
					add(new Botao(menu,"Diminuir",new AbstractAction(){
							public void actionPerformed(ActionEvent a){}
						}){{
						setIcon(new ImageIcon(getImage("Diminuir")));
						setAtalho(Event.CTRL_MASK,KeyEvent.VK_MINUS,true);
					}});
					add(new JSeparator());
		//RESTAURAR ZOOM
					add(new Botao(menu,"Restaurar para Padrão",new AbstractAction(){
							public void actionPerformed(ActionEvent a){}
						}){{
						setAtalho(Event.CTRL_MASK,KeyEvent.VK_0,true);
					}});
				}});
				add(new JSeparator());
	//RESTAURAR CÂMERA
				add(new Botao(menu,"Restaurar Câmera",new AbstractAction(){
						public void actionPerformed(ActionEvent a){}
					}){{
					setIcon(new ImageIcon(getImage("Restaurar Câmera")));
					setAtalho(Event.CTRL_MASK,KeyEvent.VK_E,true);
				}});
				add(new JSeparator());
	//QUEBRA DE LINHA
				add(lineWrap=new Toggle(menu,"Quebra Automática de Linha",new Runnable(){
						public void run(){
							tree.getTexto().setLineWrap(lineWrap.isPressed());
						}
					}){{
					setAtalho(Event.CTRL_MASK,KeyEvent.VK_Q,true);
				}});
	//MOSTRAR CARÁCTERES
				add(showAllChars=new Toggle(menu,"Mostrar Caracteres Escondidos",new Runnable(){
						public void run(){
							tree.getTexto().setViewAllChars(showAllChars.isPressed());
						}
					}){{
					setAtalho(Event.CTRL_MASK,KeyEvent.VK_Q,true);
				}});
	//SEPARAR JANELA DE TEXTO
				add(separateText=new Toggle(menu,"Separar Janela de Texto",new Runnable(){
					public void run(){
						janelaTexto.setLocked(!separateText.isPressed());
						janelaTexto.requestFocus();
					}
				}){{
				setAtalho(Event.CTRL_MASK,KeyEvent.VK_L,true);
			}});
	//AUTO-FOCAR JANELA DE TEXTO
				add(autoFocusText=new Toggle(menu,"Auto-Focar Janela de Texto",null));
				add(new JSeparator());
	//MOSTRAR GRADE
				add(showGrid=new Toggle(menu,"Mostrar Grade",new Runnable(){
					public void run(){
						tree.setShowGrid(showGrid.isPressed());
						tree.draw();
					}
				}));
			}});
//CONFIGURAR
			add(new Menu(menu,"Configurar"){{
	//FONTE
				add(new Botao(menu,"Fonte",new AbstractAction(){public void actionPerformed(ActionEvent a){
					new JFontChooser(){{
						final Frame icone=new Frame();
						icone.setIconImage(getImage("Icone"));
						if(showDialog(icone)==JFontChooser.APPROVE_OPTION)setFonte(getSelectedFont());
					}};
				}}));
	//COR DE FUNDO
				add(new Botao(menu,"Cor de Fundo",new AbstractAction(){
					public void actionPerformed(ActionEvent a){}
				}));
				add(new JSeparator());
	//LIMITE DE ENTIDADES
				add(new Botao(menu,"Limite de Entidades",new AbstractAction(){
					public void actionPerformed(ActionEvent a){}
				}));
	//LIMITE DE DESFAZER/REFAZER
				add(new Botao(menu,"Limite de Desfazer/Refazer",new AbstractAction(){
					public void actionPerformed(ActionEvent a){
						int index=0;
						switch(tree.getUndoRedoManager().getDoLimite()){
							case 0:index=0;break;
							case -1:index=doLimOpcoes.length-1;break;
							default:for(int i=1;i<doLimOpcoes.length-1;i++)if((Integer)doLimOpcoes[i]==tree.getUndoRedoManager().getDoLimite())index=i;break;
						}
						final Object opcao=JOptionPane.showInputDialog(null,
								"Limite de desfazer e refazer guardados", 
								"Limite de Desfazer/Refazer",JOptionPane.QUESTION_MESSAGE,null,doLimOpcoes,doLimOpcoes[index]);
						if(opcao instanceof Integer)tree.getUndoRedoManager().setDoLimite((Integer)opcao);
							else if(opcao instanceof String)tree.getUndoRedoManager().setDoLimite(((String)opcao).equals((String)doLimOpcoes[0])?0:-1);
					}
				}));
				add(new JSeparator());
	//SALVAR CONFIGURAÇÕES
				add(new Botao(menu,"Salvar Configurações",new AbstractAction(){public void actionPerformed(ActionEvent a){
					if((JOptionPane.showConfirmDialog(null,
							new JLabel("<html>Deseja <font color='blue'>SALVAR</font> a configuração atual?<br>")
							,"Salvar .ini",JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION)){
						setConfigIni();
						Tree.mensagem("Configuração salva!",Tree.Options.AVISO);
					}
				}}));
	//RESTAURAR CONFIGURAÇÕES
				add(new Botao(menu,"Restaurar Para Padrão",new AbstractAction(){public void actionPerformed(ActionEvent a){
					if((JOptionPane.showConfirmDialog(null,
							new JLabel("<html>Deseja <font color='red'>DELETAR</font> a configuração atual?<br>")
							,"Deletar .ini",JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION)){
						final File link=new File(System.getProperty("user.dir")+"/"+ini);
						if(link.exists()){
							link.delete();
							getConfigIni();
							Tree.mensagem("Configuração restaurada!",Tree.Options.AVISO);
						}else Tree.mensagem("Configuração .ini não encontrada!",Tree.Options.ERRO);
					}
				}}));
			}});
//PESQUISAR
			add(new Botao(menu,"Pesquisar",new AbstractAction(){public void actionPerformed(ActionEvent a){
				pesquisa.chamar();
				}}){{
				setIcon(new ImageIcon(getImage("Pesquisar")));
				setAtalho(Event.CTRL_MASK,KeyEvent.VK_F,false);
				setMaximumSize(new Dimension(100,100));
			}});
//			add(new Botao(menu,"Icones",new AbstractAction(){public void actionPerformed(ActionEvent a){
//				iconeEditor.chamar();
//				popup.show(janela,getX(),getY());
//			}}){{
////				setIcon(new ImageIcon(getImage("Icones")));
//				setMaximumSize(new Dimension(100,100));
//			}});
		}});
		add(painel=new Painel(this){{
			setBackground(Tree.FUNDO);
			add(tree=new Tree(this){{
				setFonte(painel.getJanela().getFont());
				addObjetoListener(new ObjetoFocusListener(){
					public void objetoFocused(Objeto obj){
						if(tree.getSelectedObjetos().getModulos().size()+tree.getSelectedObjetos().getConexoes().size()!=1){
							janelaTexto.setTitle("Texto");
							return;		//APENAS UM ÚNICO MOD OU COX PODE SER EDITADO POR VEZ
						}
						if(obj.getTipo().is(Objeto.Tipo.MODULO)){
							janelaTexto.setTitle(getPaiTitle(0,(Modulo)obj));
						}else if(obj.getTipo().is(Objeto.Tipo.CONEXAO)){
							final String sonTitle=((Conexao)obj).getSon().getTitle().replace("\n"," ");
							final String paiTitle=((Conexao)obj).getPai().getTitle().replace("\n"," ");
							janelaTexto.setTitle(sonTitle+" -> "+paiTitle);
						}
					}
					public void objetoUnFocused(Objeto obj){
						janelaTexto.setTitle("Texto");
					}
					private String getPaiTitle(int nivel,Modulo mod){
						if(nivel==5)return "..";
						for(Conexao cox:mod.getConexoes())if(cox.getSon()==mod)return getPaiTitle(nivel+1,cox.getPai())+"/"+mod.getTitle().replace("\n"," ");
						return mod.getTitle().replace("\n"," ");
					}
				});
				getUndoRedoManager().addUndoRedoListener(new UndoRedoListener(){
					public void actionUndone(){run();}
					public void actionRedone(){run();}
					public void actionSaved(){run();}
					private void run(){}
				});
				setVisible(true);
			}});
//			popup=new Popup(janela,tree){{
//				try{
//					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//				}catch(ClassNotFoundException|InstantiationException|IllegalAccessException|UnsupportedLookAndFeelException erro){
//					mensagem("Erro ao configurar janela!\n"+erro,ERRO);
//				}
//				for(Icone icone:icones){
//					icone.getLink();
//				}
//			}};
		}});
		addWindowListener(new WindowListener(){
			public void windowOpened(WindowEvent w){}
			public void windowIconified(WindowEvent w){}
			public void windowDeiconified(WindowEvent w){}
			public void windowClosing(WindowEvent w){if(salvarAntes())fechar();}
			public void windowClosed(WindowEvent w){}
			public void windowActivated(WindowEvent w){}
			public void windowDeactivated(WindowEvent w){}
		});
		addComponentListener(new ComponentAdapter(){
			public void componentResized(ComponentEvent r){
				if(!janela.isUndecorated())window.setSize(janela.getSize());
				tree.setSize(janela.getWidth()-janela.getInsets().left-janela.getInsets().right,
						janela.getHeight()-janela.getInsets().top-menu.getHeight()-janela.getInsets().bottom);
				tree.draw();
			}
			public void componentMoved(ComponentEvent m){
				if(!janela.isUndecorated())window.setLocation(janela.getLocation());
			}
		});
		setDropTarget(new DropTarget(){
			public void drop(DropTargetDropEvent d){
				try{
					d.acceptDrop(DnDConstants.ACTION_COPY);
					Transferable drop=d.getTransferable();
					if(drop.isDataFlavorSupported(DataFlavor.javaFileListFlavor))
						for(File file:(List<File>)drop.getTransferData(DataFlavor.javaFileListFlavor))
							if(file.getName().endsWith("mind"))
								if(salvarAntes())abrir(file);
				}catch(Exception erro){Tree.mensagem("Erro ao carregar mind!\n"+erro,Tree.Options.ERRO);}
			}
		});
		tree.getTexto().addKeyListener(new KeyAdapter(){
			private boolean lock=false;
			public void keyPressed(KeyEvent k){
				if(!isEnabled())return;
				final boolean ctrl=(k.isControlDown());
				if(!lock){
					if(!ctrl)lock=true;
					if(ctrl)switch(k.getKeyCode()){
						case KeyEvent.VK_S:
							tree.getTexto().setEnabled(false);
							if(link==null)novo(choose("Salvar"));
							salvar(link);
							tree.getTexto().setEnabled(true);
						break;
						case KeyEvent.VK_F:pesquisa.chamar();break;
					}
				}
			}
			public void keyReleased(KeyEvent k){
				if(!isEnabled())return;
				lock=false;
			}
		});
		addMouseListener(new MouseAdapter(){
			public void mousePressed(MouseEvent m){
				mousePressed=true;
			}
			public void mouseReleased(MouseEvent m){
				mousePressed=false;
			}
		});
	}};
//	private void setCorFundo(Color cor){
////		Cor.setCorFundo(cor);
//		janelaTexto.setBackground(cor);
//		painel.setBackground(cor);
//	}
	private Rectangle window=new Rectangle(janela.getBounds());
	private boolean mousePressed=false;
	private Janela janelaTexto=new Janela(janela){{
		setTitle("Texto");
		setMinimumSize(new Dimension(180,180));
		setBackground(Cor.WHITE);
		setBounds(janela.getX(),janela.getY()+janela.getHeight()-200,janela.getWidth(),200);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setAlwaysOnTop(true);
		setIconImage(getImage("Icone"));
		add(new JScrollPane(){{
			setBorder(BorderFactory.createEmptyBorder(5,5,5,0));
			setBackground(Cor.WHITE);
	        setViewportView(tree.getTexto());
			removeMouseWheelListener(getMouseWheelListeners()[0]);
			addMouseWheelListener(new MouseWheelListener(){
				public void mouseWheelMoved(MouseWheelEvent w){
					final int modifier=w.getModifiersEx(),ctrl=MouseEvent.CTRL_DOWN_MASK,right=4352;
					if(modifier==ctrl||modifier==right)getHorizontalScrollBar().setValue(getHorizontalScrollBar().getValue()+w.getWheelRotation()*40);//DIREITA-ESQUERDA
						else getVerticalScrollBar().setValue(getVerticalScrollBar().getValue()+w.getWheelRotation()*40);//CIMA-BAIXO
				}
			});
		}});
		new Thread(){
			public void run(){
				Dimension size=janelaTexto.getBounds().getSize();
				while(true){
					try{Thread.sleep(100);}catch(InterruptedException erro){}
					if(mousePressed)continue;
					if(!autoFocusText.isPressed()||(autoFocusText.isPressed()&&!size.equals(janelaTexto.getBounds().getSize()))){
						size=janelaTexto.getBounds().getSize();
						continue;
					}
					if(!janela.isFocused()&&!janelaTexto.isFocused())continue;
					if(janelaTexto.isLocked()){
						if(isJanelaTextoHover())focusJanelaTexto();else if(isJanelaHover())focusJanela();
					}else{
						if(janela.isFocused()){if(isJanelaTextoHover()&&!isJanelaHover())focusJanelaTexto();}
						else if(janelaTexto.isFocused()){if(isJanelaHover()&&!isJanelaTextoHover())focusJanela();}
					}
				}
			}
			private boolean isJanelaHover(){return janela.getBounds().contains(MouseInfo.getPointerInfo().getLocation());}
			private boolean isJanelaTextoHover(){return janelaTexto.getBounds().contains(MouseInfo.getPointerInfo().getLocation());}
			private void focusJanela(){if(!janela.isFocused())if(tree.getTitulo().isVisible())tree.getTitulo().requestFocus();else janela.requestFocus();}
			private void focusJanelaTexto(){if(!janelaTexto.isFocused())if(tree.getTexto().isEnabled())tree.getTexto().requestFocus();else janelaTexto.requestFocus();}
		}.start();
	}};
	private Searcher pesquisa=new Searcher(janela,tree,janelaTexto);
//	private IconeEditor iconeEditor=new IconeEditor(janela,tree);
	private boolean isPressed=false;
//FULLSCREEN
	private void fullscreen(boolean fullscreen){
		if(fullscreen){
			isPressed=separateText.isPressed();
			separateText.setToggle(true);
		}
		janela.dispose();
		if(!fullscreen)janela.setBackground(Tree.FUNDO);
		janela.setUndecorated(fullscreen);
		if(fullscreen)janela.setBackground(Cor.TRANSPARENTE);
		if(fullscreen){
			final Dimension screenSize=Toolkit.getDefaultToolkit().getScreenSize();
			janela.setBounds(0,0,screenSize.width,screenSize.height);
		}else janela.setBounds(window);
		janela.setAlwaysOnTop(fullscreen);
		tree.setBounds(0,janela.getInsets().top,janela.getWidth()-janela.getInsets().left-janela.getInsets().right,
				janela.getHeight()-janela.getInsets().top-janela.getInsets().bottom);
		if(!fullscreen)separateText.setToggle(isPressed);
		tree.draw();
		janela.setVisible(true);
	}
//ICONES
	private Image getImage(String nome){
		return Toolkit.getDefaultToolkit().getImage(getClass().getResource("/icons/"+nome+".png"));
	}
//FILE CHOOSER
	private File choose(String nome){
		final JFileChooser choose=new JFileChooser(){
			public void approveSelection(){
				if(getSelectedFile().exists()){
					switch(JOptionPane.showConfirmDialog(this,getSelectedFile().getName()+" já existe.\nDeseja substituí-lo?","Arquivo já existe",JOptionPane.YES_NO_OPTION)){
						case JOptionPane.YES_OPTION:super.approveSelection();return;
						case JOptionPane.NO_OPTION:return;
						case JOptionPane.CLOSED_OPTION:return;
					}
				}
				super.approveSelection();
			}{
			setFileFilter(new FileNameExtensionFilter("Mind Map","mind"));
			setAcceptAllFileFilterUsed(false);
		}};
		final Frame icone=new Frame();
		icone.setIconImage(getImage(nome));
		return (choose.showDialog(icone,nome)==JFileChooser.APPROVE_OPTION?choose.getSelectedFile():null);
	}
//NOVO
	private void novo(File mind){
		if(mind==null)return;
		if(!mind.toString().endsWith(".mind"))mind=new File(mind+".mind");
		try{
			final PrintWriter writer=new PrintWriter(this.link=mind,"UTF-8");
			writer.println("<mind fontName=\""+Tree.Fonte.FONTE.getName()+"\" fontStyle=\""+Tree.Fonte.FONTE.getStyle()+"\" fontSize=\""+Tree.Fonte.FONTE.getSize()+"\">");
			writer.println("<mod title=\"Novo Mind\" x=\"0\" y=\"0\" color=\"(0,255,255)\" icons=\"\"></mod>");
			writer.println("</mind>");
			writer.close();
		}catch(Exception erro){Tree.mensagem("Erro ao criar .mind!\n"+erro,Tree.Options.ERRO);}
		janela.setTitle(titulo+" - "+link);
	}
//ABRIR
	private void abrir(File mind){
		if(mind==null)return;
		new Thread(new Runnable(){
			public void run(){
				tree.setVisible(false);
				tree.clear();
				try{
					final Document tags=DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(link=mind);
					final Element mindTag=tags.getDocumentElement();
					tree.addTree(mindTag,true);
				}catch(Exception erro){Tree.mensagem("Erro ao abrir .mind!\n"+erro,Tree.Options.ERRO);}
				tree.unSelectAll();
				tree.setFocusOn(new Objeto[]{Tree.getMestre()});
				janela.setTitle(mind.getName()+" - "+link);
				tree.setVisible(true);
				tree.draw();
			}
		}).start();
	}
//SALVAR
	boolean saving=false;
	private void salvar(File mind){
		saving=true;
		salvar(mind,5);	//TENTA SALVAR 5 VEZES
		saving=false;
	}
	private void salvar(File mind,int tentativas){
		if(mind==null)return;
		if(!mind.exists()){		//CRIAR NOVO ARQUIVO
			writeFile(mind);			//CRIA NOVO MIND
			if(!mind.exists())salvar(mind,tentativas-1);		//RETRY
		}else{					//SALVAR ALTERAÇÕES
			final File tempMind=new File(mind.toString()+".temp");
			try{						//COPIA MIND PARA TEMP_MIND
				Files.copy(mind.toPath(),tempMind.toPath(),StandardCopyOption.REPLACE_EXISTING);
			}catch(IOException erro){Tree.mensagem("Erro ao criar .temp!\n"+erro,Tree.Options.ERRO);}
			if(!tempMind.exists())salvar(mind,tentativas-1);	//RETRY
			writeFile(mind);			//SALVA ALTERAÇÕES EM MIND
			if(mind.exists()&&tempMind.exists()){
				tempMind.delete();		//APAGA TEMP_MIND
			}else salvar(mind,tentativas-1);					//RETRY
		}
		if(janela.getTitle().startsWith("*"))janela.setTitle(janela.getTitle().substring(1));
		
//		final File newMind=new File(mind.toString());				//COPIA MIND
//		if(mind.exists())mind=new File(mind.toString()+".temp");	//MIND ORIGINAL VIRA .TEMP
//		try{														//SALVA ALTERAÇÕES EM .TEMP
//			final BufferedWriter mindFile=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(mind),StandardCharsets.UTF_8));
//			mindFile.write(tree.getText(tree.getAllModulos()));
//			mindFile.close();
//		}catch(IOException erro){Tree.mensagem("Erro ao salvar .mind!\n"+erro,Tree.ERRO);}
//		if(newMind.exists()&&mind.exists()){
//			newMind.delete();										//APAGA MIND
//			mind.renameTo(newMind);									//.TEMP VIRA O NOVO MIND
//		}else salvar(newMind);										//LOOP ATÉ SALVAR DE FATO
//		if(janela.getTitle().startsWith("*"))janela.setTitle(janela.getTitle().substring(1));
	}
	private boolean salvarAntes(){
		if(!janela.getTitle().startsWith("*"))return true;
		switch(JOptionPane.showConfirmDialog(null,new JLabel("<html>Deseja <font color='blue'>SALVAR</font>?<br>"),"Salvar .mind",JOptionPane.YES_NO_CANCEL_OPTION)){
			case JOptionPane.YES_OPTION:
				if(link==null)novo(choose("Salvar"));
				salvar(link);
			break;
			case JOptionPane.NO_OPTION:
				janela.setTitle(janela.getTitle().substring(1));
			break;
			case JOptionPane.CANCEL_OPTION:case JOptionPane.CLOSED_OPTION:return false;
		}
		return true;
	}
	private void writeFile(File mind){
		try{
			final BufferedWriter mindFile=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(mind),StandardCharsets.UTF_8));
			mindFile.write(tree.getText(tree.getObjetos()));
			mindFile.close();
		}catch(IOException erro){Tree.mensagem("Erro ao salvar .mind!\n"+erro,Tree.Options.ERRO);}
	}
//FECHAR
	private void fechar(){
		if(!saving)System.exit(0);
	}
	private void getConfigIni(){
		final File link=new File(System.getProperty("user.dir")+"/"+ini);
		try{
			Rectangle treeWindowArea=janela.getBounds();
			Rectangle textoWindowArea=new Rectangle(treeWindowArea.x,treeWindowArea.y+400,treeWindowArea.width,treeWindowArea.height-400);
			int textoDialogWidth=textoWindowArea.width,textoDialogHeight=textoWindowArea.height;
			Font fonte=this.fonte;
			Cor corFundo=Tree.FUNDO;
			Cor[][]coresPaleta=new Cor[][]{
				new Cor[]{new Cor(178,000,255),	new Cor(000,200,033),	new Cor(255,000,000),	new Cor(000,000,255),	new Cor(000,000,000)},
				new Cor[]{new Cor(255,000,220),	new Cor(076,255,000),	new Cor(255,106,000),	new Cor(110,000,255),	new Cor(100,100,100)},
				new Cor[]{null,					null,					new Cor(255,178,000),	new Cor(000,148,255),	new Cor(220,220,220)},
				new Cor[]{null,					null,					new Cor(255,255,000),	new Cor(000,255,255),	new Cor(255,255,255)}
			};
			boolean mostrarGrade=false,quebrarLinhas=false,separarTextoWindow=false,autoFocarTextoWindow=false;
			int entidadeLimite=100,undoRedoLimite=100;
			File iconesPasta=new File(System.getProperty("user.dir")+"/Icones");
			if(link.exists())for(String linha:Files.readAllLines(link.toPath(),StandardCharsets.UTF_8)){
				switch(linha.substring(0,linha.indexOf("=")+1)){
					//ÁREA DA JANELA
					case "xTreeWindow=":treeWindowArea.x=getInteger(linha);break;
					case "yTreeWindow=":treeWindowArea.y=getInteger(linha);break;
					case "widthTreeWindow=":treeWindowArea.width=getInteger(linha);break;
					case "heightTreeWindow=":treeWindowArea.height=getInteger(linha);break;
					//ÁREA DA JANELA DO TEXTO
					case "xTextoWindow=":textoWindowArea.x=getInteger(linha);break;
					case "yTextoWindow=":textoWindowArea.y=getInteger(linha);break;
					case "widthTextoWindow=":textoWindowArea.width=getInteger(linha);break;
					case "heightTextoWindow=":textoWindowArea.height=getInteger(linha);break;
					//ÁREA DO DIALOG DO TEXTO
					case "widthTextoDialog=":textoDialogWidth=getInteger(linha);break;
					case "heigthTextoDialog=":textoDialogHeight=getInteger(linha);break;
					//FONTE
					case "fonte=":fonte=getFont(linha);break;
					//CORES
					case "corFundo=":corFundo=getCor(linha);break;
					case "cor_0_0=":coresPaleta[0][0]=getCor(linha);break;
					case "cor_0_1=":coresPaleta[0][1]=getCor(linha);break;
					case "cor_0_2=":coresPaleta[0][2]=getCor(linha);break;
					case "cor_0_3=":coresPaleta[0][3]=getCor(linha);break;
					case "cor_0_4=":coresPaleta[0][4]=getCor(linha);break;
					case "cor_1_0=":coresPaleta[1][0]=getCor(linha);break;
					case "cor_1_1=":coresPaleta[1][1]=getCor(linha);break;
					case "cor_1_2=":coresPaleta[1][2]=getCor(linha);break;
					case "cor_1_3=":coresPaleta[1][3]=getCor(linha);break;
					case "cor_1_4=":coresPaleta[1][4]=getCor(linha);break;
					case "cor_2_2=":coresPaleta[2][2]=getCor(linha);break;
					case "cor_2_3=":coresPaleta[2][3]=getCor(linha);break;
					case "cor_2_4=":coresPaleta[2][4]=getCor(linha);break;
					case "cor_3_2=":coresPaleta[3][2]=getCor(linha);break;
					case "cor_3_3=":coresPaleta[3][3]=getCor(linha);break;
					case "cor_3_4=":coresPaleta[3][4]=getCor(linha);break;
					//BOOLS
					case "showGrid=":mostrarGrade=getBoolean(linha);break;
					case "quebrarLinhas=":quebrarLinhas=getBoolean(linha);break;
					case "separarTextoWindow=":separarTextoWindow=getBoolean(linha);break;
					case "autoFocarTextoWindow=":autoFocarTextoWindow=getBoolean(linha);break;
					//LIMITES
					case "entidadeLimite=":entidadeLimite=getInteger(linha);break;
					case "undoRedoLimite=":undoRedoLimite=getInteger(linha);break;
					//ICONE
					case "iconesPasta=":iconesPasta=new File(getString(linha));break;
				}
			}
			janela.setBounds(treeWindowArea);
			janelaTexto.setBounds(textoWindowArea);
			janelaTexto.setDialogDefaultWidth(textoDialogWidth);
			janelaTexto.setDialogDefaultHeight(textoDialogHeight);
			setFonte(fonte);
//			Cor.setCorFundo(corFundo);
			tree.getPopup().setCoresPaleta(coresPaleta);
			showGrid.setToggle(mostrarGrade);
			lineWrap.setToggle(quebrarLinhas);
			separateText.setToggle(separarTextoWindow);
			autoFocusText.setToggle(autoFocarTextoWindow);
//			tree.setEntidadeLimite(entidadeLimite);
			tree.getUndoRedoManager().setDoLimite(undoRedoLimite);
			tree.getPopup().setIconePasta(iconesPasta);
		}catch(IOException erro){Tree.mensagem("Erro ao configurar sistema!\n"+erro,Tree.Options.ERRO);}
	}
	private String getString(String linha){return linha.substring(linha.indexOf("=")+1,linha.length());}
	private int getInteger(String linha){return Integer.parseInt(getString(linha));}
	private Font getFont(String linha){
		String[]fonte=getString(linha).split(",");
		return new Font(fonte[0],Integer.parseInt(fonte[1]),Integer.parseInt(fonte[2]));
	}
	private Cor getCor(String linha){
		final String corHex=getString(linha);
		return new Cor(Integer.valueOf(corHex.substring(1,3),16),Integer.valueOf(corHex.substring(3,5),16),Integer.valueOf(corHex.substring(5,7),16));
	}
	private boolean getBoolean(String linha){
		final String bool=getString(linha);
		return(bool.equals("true")?true:bool.equals("false")?false:null);
	}
	private void setConfigIni(){
		final File link=new File(System.getProperty("user.dir")+"/"+ini);
		try{
			PrintWriter writer=new PrintWriter(link,"UTF-8");
			writer.println("[Settings]");
			writer.println("[Locais]");
			writer.println("xTreeWindow="+janela.getX());
			writer.println("yTreeWindow="+janela.getY());
			writer.println("widthTreeWindow="+janela.getWidth());
			writer.println("heightTreeWindow="+janela.getHeight());
			writer.println("xTextoWindow="+janelaTexto.getX());
			writer.println("yTextoWindow="+janelaTexto.getY());
			writer.println("widthTextoWindow="+janelaTexto.getWidth());
			writer.println("heightTextoWindow="+janelaTexto.getHeight());
			writer.println("widthTextoDialog="+janelaTexto.getDialogDefaultWidth());
			writer.println("heigthTextoDialog="+janelaTexto.getDialogDefaultHeight());
			writer.println("[Fonte]");
			writer.println("fonte="+fonte.getName()+","+fonte.getStyle()+","+fonte.getSize());
			writer.println("[Fundo]");
			writer.println("corFundo="+("#"+Integer.toHexString(Tree.FUNDO.getRGB()).substring(2).toUpperCase()));
			writer.println("showGrid="+showGrid.isPressed());
			writer.println("[Cores Paleta]");
//			writer.println("cor_0_0="+("#"+Integer.toHexString(ColorPick.PALETA_DEFAULT[0][0].getRGB()).substring(2).toUpperCase()));
//			writer.println("cor_0_1="+("#"+Integer.toHexString(ColorPick.PALETA_DEFAULT[0][1].getRGB()).substring(2).toUpperCase()));
//			writer.println("cor_0_2="+("#"+Integer.toHexString(ColorPick.PALETA_DEFAULT[0][2].getRGB()).substring(2).toUpperCase()));
//			writer.println("cor_0_3="+("#"+Integer.toHexString(ColorPick.PALETA_DEFAULT[0][3].getRGB()).substring(2).toUpperCase()));
//			writer.println("cor_0_4="+("#"+Integer.toHexString(ColorPick.PALETA_DEFAULT[0][4].getRGB()).substring(2).toUpperCase()));
//			writer.println("cor_1_0="+("#"+Integer.toHexString(ColorPick.PALETA_DEFAULT[1][0].getRGB()).substring(2).toUpperCase()));
//			writer.println("cor_1_1="+("#"+Integer.toHexString(ColorPick.PALETA_DEFAULT[1][1].getRGB()).substring(2).toUpperCase()));
//			writer.println("cor_1_2="+("#"+Integer.toHexString(ColorPick.PALETA_DEFAULT[1][2].getRGB()).substring(2).toUpperCase()));
//			writer.println("cor_1_3="+("#"+Integer.toHexString(ColorPick.PALETA_DEFAULT[1][3].getRGB()).substring(2).toUpperCase()));
//			writer.println("cor_1_4="+("#"+Integer.toHexString(ColorPick.PALETA_DEFAULT[1][4].getRGB()).substring(2).toUpperCase()));
//			writer.println("cor_2_2="+("#"+Integer.toHexString(ColorPick.PALETA_DEFAULT[2][2].getRGB()).substring(2).toUpperCase()));
//			writer.println("cor_2_3="+("#"+Integer.toHexString(ColorPick.PALETA_DEFAULT[2][3].getRGB()).substring(2).toUpperCase()));
//			writer.println("cor_2_4="+("#"+Integer.toHexString(ColorPick.PALETA_DEFAULT[2][4].getRGB()).substring(2).toUpperCase()));
//			writer.println("cor_3_2="+("#"+Integer.toHexString(ColorPick.PALETA_DEFAULT[3][2].getRGB()).substring(2).toUpperCase()));
//			writer.println("cor_3_3="+("#"+Integer.toHexString(ColorPick.PALETA_DEFAULT[4][3].getRGB()).substring(2).toUpperCase()));
//			writer.println("cor_3_4="+("#"+Integer.toHexString(ColorPick.PALETA_DEFAULT[5][4].getRGB()).substring(2).toUpperCase()));
			writer.println("[Texto]");
			writer.println("quebrarLinhas="+lineWrap.isPressed());
			writer.println("separarTextoWindow="+separateText.isPressed());
			writer.println("autoFocarTextoWindow="+autoFocusText.isPressed());
			writer.println("[Limites]");
//			writer.println("entidadeLimite="+tree.getEntidadeLimite());
			writer.println("undoRedoLimite="+tree.getUndoRedoManager().getDoLimite());
			writer.println("[Icones]");
			writer.println("iconesPasta="+tree.getPopup().getIconePasta());
			writer.close();
		}catch(Exception erro){Tree.mensagem("Erro ao salvar .ini!\n"+erro,Tree.Options.ERRO);}
	}
}