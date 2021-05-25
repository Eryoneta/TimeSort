package main;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import element.tree.popup.color.CorBloco;
import element.tree.Cor;
import element.tree.Tree;
@SuppressWarnings("serial")
public class IconeEditor{
	private JDialog dialog;
//	private ModuloTree tree;
	private JTextField nome;
	private Paleta paleta;
	private IconEditor editor;
	private CorBloco focado;
	public IconeEditor(JFrame janela,Tree tree){
//		this.tree=tree;
		dialog=new JDialog(janela){{
			final JDialog dialog=this;
			setTitle("Icon Editor");
			final int width=700;
			setSize(width,(int)(width*0.7));
//			setResizable(false);
			setLocationRelativeTo(janela);
			getContentPane().add(new GridPainel(){{
				final GridPainel grid=this;
				setGrid(dialog.getWidth()/3,dialog.getHeight()/3);
				setSize(dialog.getSize());
				final int borda=5;
				final JButton amostra=new JButton("A"){{
					setSize(10,10);
					addActionListener(new ActionListener(){
						public void actionPerformed(ActionEvent a){
							
						}
					});
				}};
//				paleta=new Paleta(){{
//					setGrid((4*2)+2,(4*16)+2);
//					setBounds(borda,borda,20,grid.getGridRow()-(borda*2)-amostra.getHeight()-borda);
//					setBorder(BorderFactory.createEtchedBorder());
//					focado=getCor(0,0);
//					for(CorBloco[]row:getCores())for(CorBloco col:row){
//						col.addMouseMotionListener(new MouseMotionAdapter() {
//							public void mouseMoved(MouseEvent m) {
//								unfocus();
//								focado.setFocus(true);
//								col.setFocus(true);
//								repaint();
//							}
//						});
//						col.addMouseListener(new MouseAdapter(){
//							private boolean pressed=false;
//							public void mousePressed(MouseEvent m){
//								final int botao=m.getButton(),left=MouseEvent.BUTTON1,right=MouseEvent.BUTTON3;
//								switch(botao){
//									case left:case right:
//										pressed=true;
//									break;
//								};
//							}
//							public void mouseReleased(MouseEvent m){
//								final int botao=m.getButton(),left=MouseEvent.BUTTON1,right=MouseEvent.BUTTON3;
//								if(pressed)switch(botao){
//									case left:
//										focado=col;
//									break;
//									case right:
//										final Color cor=JColorChooser.showDialog(null,"Escolha uma cor",col.getColor());
//										if(cor!=null)col.setColor(new Cor(cor.getRed(),cor.getGreen(),cor.getBlue(),cor.getAlpha()));
//										repaint();
//										editor.repaint();
//									break;
//								};
//								pressed=false;
//							}
//						});
//					}
//				}};
				nome=new JTextField(){{
					setLocation(borda+paleta.getWidth()+borda,borda);
					setSize(grid.getGridCol()-getX()-borda,10);
				}};
				editor=new IconEditor(){{
					setGrid(4*16,4*16);
					setLocation(borda+paleta.getWidth()+borda,borda+nome.getHeight()+borda);
					final int size=paleta.getHeight()-nome.getHeight()-borda;
					setSize(size,size);
					setBorder(BorderFactory.createEtchedBorder());
					for(Pixel[]row:getPixels())for(Pixel col:row)col.setCorBloco(focado);
					addMouseMotionListener(new MouseMotionAdapter(){
						public void mouseDragged(MouseEvent m){
							final Pixel pixel=getPixel((int)(m.getPoint().y/4/getSquareHeight()),(int)(m.getPoint().x/4/getSquareWidth()));
							if(pixel!=null)pixel.setCorBloco(focado);
							repaint();
						}
					});
					addMouseListener(new MouseAdapter(){
						public void mousePressed(MouseEvent m){
							final Pixel pixel=getPixel((int)(m.getPoint().y/4/getSquareHeight()),(int)(m.getPoint().x/4/getSquareWidth()));
							if(pixel!=null)pixel.setCorBloco(focado);
							repaint();
						}
					});
				}};
				amostra.setLocation(borda,borda+paleta.getHeight()+borda);
				final JButton balde=new JButton("B"){{
					setSize(amostra.getSize());
					setLocation(amostra.getX()+amostra.getWidth()+borda,amostra.getY());
					addActionListener(new ActionListener(){
						public void actionPerformed(ActionEvent a){
							
						}
					});
				}};
				final JButton lapis=new JButton("L"){{
					setSize(amostra.getSize());
					setLocation(balde.getX()+balde.getWidth()+borda,amostra.getY());
					addActionListener(new ActionListener(){
						public void actionPerformed(ActionEvent a){
							
						}
					});
				}};
				final JButton excluir=new JButton("E"){{
					setSize(20,amostra.getHeight());
					setLocation(grid.getGridCol()-borda-getWidth(),amostra.getY());
					addActionListener(new ActionListener(){
						public void actionPerformed(ActionEvent a){
							
						}
					});
				}};
				final JButton cancel=new JButton("C"){{
					setSize(excluir.getSize());
					setLocation(excluir.getX()-borda-getWidth(),amostra.getY());
					addActionListener(new ActionListener(){
						public void actionPerformed(ActionEvent a){
							
						}
					});
				}};
				final JButton ok=new JButton("O"){{
					setSize(excluir.getSize());
					setLocation(cancel.getX()-borda-getWidth(),amostra.getY());
					addActionListener(new ActionListener(){
						public void actionPerformed(ActionEvent a){
//							System.out.println(getCores()+getPixels());
						}
					});
				}};
				addOnGrid(paleta);
				addOnGrid(nome);
				addOnGrid(editor);
				addOnGrid(amostra);
				addOnGrid(balde);
				addOnGrid(lapis);
				addOnGrid(ok);
				addOnGrid(cancel);
				addOnGrid(excluir);
				addMouseMotionListener(new MouseMotionAdapter() {
					public void mouseMoved(MouseEvent m) {
						paleta.unfocus();
						focado.setFocus(true);
					}
				});
			}});
			addComponentListener(new ComponentAdapter(){
				public void componentResized(ComponentEvent r){
					setSize(getWidth(),(int)(getWidth()*0.7));
				}
			});
//			try{
//				setEditor("Teste","FFFFFF44666666FFCCCCCCFFDDD9DBFF3399FFFF33CCFFFF00FFFFFFFF3333FFFFFF00FF000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000011167007070700001401677070700000134016070700000091341670707000000911416707000000a099141670700000c000914167070000caa0091416700000ccaa009141670000acca000914160000aaccaa0091416000caac0ac009141660ccac0acc009141160cccccaaa0091441cc0caccca00914410cccccacccaa9110");	
//			}catch(Exception erro){}
		}};
	}
	public void chamar(){dialog.setVisible(true);}
	public void dispensar(){dialog.dispose();}
	
	public void setEditor(String nome,String dados)throws Exception{
		this.nome.setText(nome);
		final Matcher base16=Pattern.compile("([0-9A-Fa-f]{256})").matcher(dados);
		if(base16.find())setCores(base16.group(1));
		final Matcher base32=Pattern.compile("([0-9A-Va-v]{256})").matcher(dados);
		base32.find();		//IGNORA HEX
		if(base32.find())setPixels(base32.group(1));
	}
	public void setCores(String dados)throws Exception{
		final Matcher cores=Pattern.compile("([0-9A-Fa-f]{2}[0-9A-Fa-f]{2}[0-9A-Fa-f]{2}[0-9A-Fa-f]{2})").matcher(dados);
		for(CorBloco[]row:paleta.getCores())for(CorBloco col:row){
			cores.find();
			col.setCor(Cor.HexToRGBA(cores.group(1)));
		}
	}
	public void setPixels(String dados)throws Exception{
		final Matcher pixels=Pattern.compile("([0-9A-va-v]{1})").matcher(dados);
		for(Pixel[]row:editor.getPixels())for(Pixel col:row){
			pixels.find();
			col.setCorBloco(getCorBlocoByIndex(Integer.valueOf(pixels.group(1),32)));
		}
	}
	private CorBloco getCorBlocoByIndex(int index){
		for(int i=0,y=0;y<paleta.getCores().length;y++,i++)
			for(int x=0;x<paleta.getCores()[y].length;x++,i++)
				if(i==index)return paleta.getCores()[y][x];
		return null;
	}
	public String getNome(){return nome.getText();}
	public String getCores(){
		String cores="";
		for(CorBloco[]row:paleta.getCores())for(CorBloco col:row)cores+=col.getCor().toHexAlpha();
		return cores;
	}
	public String getPixels(){
		String cores="";
		for(Pixel[]row:editor.getPixels())for(Pixel col:row)cores+=Integer.toString(getIndexByCorBloco(col),32);
		return cores;
	}
	private int getIndexByCorBloco(Pixel pixel){
		for(int i=0,y=0;y<paleta.getCores().length;y++,i++)
			for(int x=0;x<paleta.getCores()[y].length;x++,i++)
				if(paleta.getCores()[y][x]==pixel.getCorBloco())return i;
		return 0;
	}
	public void addToXML(Document xml,Element mindTag){
		final Element iconTag=xml.createElement("icon");	//ICON
		iconTag.setAttribute("name",getNome());				//NAME
		iconTag.setTextContent(getCores()+getPixels());		//CORES E PIXELS
		iconTag.appendChild(iconTag);
	}
}
@SuppressWarnings("serial")
class Paleta extends GridPainel{
	private CorBloco[][]cores=new CorBloco[16][2];
		public CorBloco[][]getCores(){return cores;}
		public CorBloco getCor(int y,int x){if(y<0||y>=cores.length||x<0||x>=cores[0].length)return null;else return cores[y][x];}
		public void unfocus(){for(CorBloco[]row:cores)for(CorBloco col:row)col.setFocus(false);repaint();}
	public Paleta(){
		super();
//		for(int y=0;y<16;y++)for(int x=0;x<2;x++)cores[y][x]=(CorBloco)addOnGrid(new CorBloco(1+(4*x),1+(4*y),4,Cor.TRANSPARENTE));
		getCor(0,0).setFocus(true);
	}
	public void paint(Graphics imagemEdit){
		super.paint(imagemEdit);
		for(Component comp:getComponents())comp.paint(imagemEdit);
	}
}
@SuppressWarnings("serial")
class IconEditor extends GridPainel{
	private Pixel[][]pixels=new Pixel[16][16];
		public Pixel[][]getPixels(){return pixels;}
		public Pixel getPixel(int y,int x){if(y<0||y>=pixels.length||x<0||x>=pixels[0].length)return null;else return pixels[y][x];}
	public IconEditor(){
		super();
		for(int y=0;y<16;y++)for(int x=0;x<16;x++)pixels[y][x]=(Pixel)addOnGrid(new Pixel((4*x),(4*y),4,new CorBloco(0,0,0,Cor.TRANSPARENTE)));
	}
	public void paint(Graphics imagemEdit){
		super.paint(imagemEdit);
		for(Component comp:getComponents())comp.paint(imagemEdit);
	}
}
@SuppressWarnings("serial")
class Pixel extends Component{
	private CorBloco corBloco;
		public CorBloco getCorBloco(){return corBloco;}
		public void setCorBloco(CorBloco corBloco){this.corBloco=corBloco;}
	public Pixel(int x,int y,int size,CorBloco corBloco){
		setBounds(x,y,size,size);
		setCorBloco(corBloco);
	}
	public void paint(Graphics imagemEdit){
		imagemEdit.setColor(getCorBloco().getCor());
		imagemEdit.fillRect((int)getX(),(int)getY(),(int)getWidth(),(int)getHeight());
		imagemEdit.setColor(new Color(0,0,0,50));
		imagemEdit.drawRect((int)getX(),(int)getY(),(int)getWidth(),(int)getHeight());
	}
}
@SuppressWarnings("serial")
class GridPainel extends JPanel{
	private HashMap<Component,Rectangle>sizes=new HashMap<>();
		public Rectangle getComponentIndex(Component comp){return sizes.get(comp);}
	public double getSquareWidth(){return (double)getWidth()/col;}
	public double getSquareHeight(){return (double)getHeight()/row;}
	private int col=1,row=1;
		public Dimension getGrid(){return new Dimension(col,row);}
		public int getGridCol(){return col;}
		public int getGridRow(){return row;}
		public void setGrid(int col,int row){this.col=col;this.row=row;}
	public GridPainel(){
		setLayout(null);
		addComponentListener(new ComponentAdapter(){
			public void componentResized(ComponentEvent r){update();}
		});
	}
	public Component addOnGrid(Component comp){
		super.add(comp);
		sizes.put(comp,comp.getBounds());
		comp.setBounds(getBoundOnGrid(comp.getBounds()));
		return comp;
	}
	private void update(){for(Component comp:getComponents())comp.setBounds(getBoundOnGrid(getComponentIndex(comp)));}
	private Rectangle getBoundOnGrid(Rectangle comp){
		final double width=getSquareWidth(),height=getSquareHeight();
		return new Rectangle((int)(comp.x*width),(int)(comp.y*height),(int)(comp.width*width),(int)(comp.height*height));
	}
}