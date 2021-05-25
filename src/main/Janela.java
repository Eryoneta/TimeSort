package main;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.JDialog;
import javax.swing.JFrame;
@SuppressWarnings("serial")
public class Janela{
	private JFrame janelaPai;
	private JFrame janela;
	private JDialog dialog;
	public void add(Component elemento){
		if(isLocked())dialog.add(elemento);else janela.add(elemento);
	}
	public void remove(Component elemento){
		if(isLocked())dialog.remove(elemento);else janela.remove(elemento);
	}
	public void setTitle(String titulo){
		janela.setTitle(titulo);
		dialog.setTitle(titulo);
	}
	public void setMinimumSize(Dimension size){
		janela.setMinimumSize(size);
		dialog.setMinimumSize(size);
	}
	public void setBackground(Color cor){
		janela.setBackground(cor);
		dialog.setBackground(cor);
	}
	public int getX(){return janela.getX();}
	public int getY(){return janela.getY();}
	public int getWidth(){return janela.getWidth();}
	public int getHeight(){return janela.getHeight();}
	public void setBounds(int x,int y,int width,int height){
		janela.setBounds(x,y,width,height);
		dialog.setBounds(x,y,width,height);
	}
	public void setBounds(Rectangle area){
		janela.setBounds(area);
		dialog.setBounds(area);
	}
	public Rectangle getBounds(){return (isLocked()?dialog.getBounds():janela.getBounds());}
	public void setDefaultCloseOperation(int operation){
		janela.setDefaultCloseOperation(operation);
		dialog.setDefaultCloseOperation(operation);
	}
	public void setAlwaysOnTop(boolean topo){
		dialog.setAlwaysOnTop(topo);
	}
	public void setIconImage(Image imagem){
		janela.setIconImage(imagem);
		dialog.setIconImage(imagem);
	}
	public Janela(JFrame janelaPai){
		this.janelaPai=janelaPai;
		janela=new JFrame(){{
			setFont(janelaPai.getFont());
		}};
		dialog=new JDialog(janelaPai){{
			setFont(janelaPai.getFont());
		}};
		janelaPai.addComponentListener(new ComponentAdapter(){
			public void componentResized(ComponentEvent r){
				if(isLocked())setBounds();
			}
			public void componentMoved(ComponentEvent m){
				if(isLocked())setBounds();
			}
		});
		dialog.addComponentListener(new ComponentAdapter(){
			public void componentResized(ComponentEvent r){
				setBounds();
			}
		});
	}
	public void setLocked(boolean locked){
		if(locked)dialog.setContentPane(janela.getContentPane());else janela.setContentPane(dialog.getContentPane());
		janela.setVisible(!locked);
		dialog.setVisible(locked);
		janela.getContentPane().revalidate();
		setBounds();
	}
	public boolean isLocked(){return (!janela.isVisible()&&dialog.isVisible());}
	private int dialogDefaultWidth;
		public int getDialogDefaultWidth(){return dialogDefaultWidth;}
		public void setDialogDefaultWidth(int width){dialogDefaultWidth=width;}
	private int dialogDefaultHeight;
		public int getDialogDefaultHeight(){return dialogDefaultHeight;}
		public void setDialogDefaultHeight(int height){dialogDefaultHeight=height;}
	private boolean horizontal=false;
	private void setBounds(){
		if(janelaPai.getWidth()>janelaPai.getHeight()){//HORIZONTAL
			final int width=(horizontal==false?dialogDefaultWidth:Math.min(janelaPai.getWidth()-(janelaPai.getWidth()/5),dialog.getWidth()));
			dialog.setSize(width,janelaPai.getHeight()-janelaPai.getInsets().top);
			dialogDefaultWidth=width;
			horizontal=true;
			dialog.setLocation(janelaPai.getX()+janelaPai.getWidth()-dialog.getWidth(),janelaPai.getY()+janelaPai.getInsets().top);
		}else{//VERTICAL
			final int height=(horizontal==true?dialogDefaultHeight:Math.min(janelaPai.getHeight()-(janelaPai.getHeight()/5),dialog.getHeight()));
			dialog.setSize(janelaPai.getWidth(),height);
			dialogDefaultHeight=height;
			horizontal=false;
			dialog.setLocation(janelaPai.getX(),janelaPai.getY()+janelaPai.getHeight()-dialog.getHeight());
		}
		dialog.revalidate();
		janelaPai.requestFocus();
	}
	public void requestFocus(){
		janela.requestFocus();
		dialog.requestFocus();
	}
	public boolean isFocused(){return (janela.isFocused()||dialog.isFocused());}
}