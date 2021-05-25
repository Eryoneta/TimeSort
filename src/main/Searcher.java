package main;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import element.tree.objeto.Objeto;
import element.tree.objeto.modulo.Modulo;
import element.tree.texto.ObjetoSetListener;
import element.tree.Tree;
@SuppressWarnings("serial")
public class Searcher{
	private JDialog dialog;
	private Tree tree;
	private Janela janelaTexto;
	public Searcher(JFrame janela,Tree tree,Janela janelaTexto){
		this.tree=tree;
		this.janelaTexto=janelaTexto;
		dialog=new JDialog(janela){{
			setTitle("Procurar");
			setSize(350,300);
			setMinimumSize(getSize());
			setLocationRelativeTo(janela);
			final JTextField termo=new JTextField();//TEXTO
			final ButtonGroup grupoDirecao=new ButtonGroup(),grupoEscopo=new ButtonGroup();
			final JRadioButton frente=new JRadioButton("Frente"){{//SELEC FRENTE
				grupoDirecao.add(this);
				setSelected(true);
			}};
			final JRadioButton atras=new JRadioButton("Atrás"){{//SELEC ATRÁS
				grupoDirecao.add(this);
			}};
			final JRadioButton tudo=new JRadioButton("Tudo"){{//SELEC TUDO
				grupoEscopo.add(this);
				setSelected(true);
				addItemListener(new ItemListener(){public void itemStateChanged(ItemEvent i){reset();}});
			}};
			final JRadioButton onlySelected=new JRadioButton("Somente seleção"){{//SELEC SELEC
				grupoEscopo.add(this);
				addItemListener(new ItemListener(){public void itemStateChanged(ItemEvent i){reset();}});
			}};
			final JCheckBox wholeWord=new JCheckBox("Palavra inteira");//CHECK PALAVRA-INT
			final JCheckBox diffMaiuscMinusc=new JCheckBox("Diferenciar maiúscula de minúscula");//CHECK DIFF-MAISC-MINUS
			getContentPane().add(new JPanel(){{//PAINEL TEXTO
				setLayout(new GridLayout(1,2));
				add(new JLabel("Procurar:"){{
		            setHorizontalAlignment(JLabel.LEFT);
		            setHorizontalTextPosition(JLabel.LEFT);
		            setLabelFor(termo);
				}});
				add(termo);
				setBorder(BorderFactory.createEmptyBorder(25,10,20,10));
			}},BorderLayout.NORTH);
			getContentPane().add(new JPanel(){{//PAINEL TODAS-OPÇÕES
				setLayout(new GridLayout(2,1));
				add(new JPanel(){{//PAINEL OPÇÕES-LATERAL
					setLayout(new GridLayout(1,2));
					add(new JPanel(){{//PAINEL DIREÇÃO
						setLayout(new GridLayout(2,1));
						setBorder(BorderFactory.createCompoundBorder(
							BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),"Direção"),
							BorderFactory.createEmptyBorder(5,10,10,10)
						));
						add(frente);
						add(atras);
					}});
					add(new JPanel(){{//PAINEL ESCOPO
						setLayout(new GridLayout(2,1));
						setBorder(BorderFactory.createCompoundBorder(
							BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),"Escopo"),
							BorderFactory.createEmptyBorder(5,10,10,10)
						));
						add(tudo);
						add(onlySelected);
					}});
				}});
				add(new JPanel(){{//PAINEL OPÇÕES
					setLayout(new GridLayout(2,1));
					setBorder(BorderFactory.createCompoundBorder(
						BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),"Opções"),
						BorderFactory.createEmptyBorder(5,10,10,10)
					));
					add(wholeWord);
					add(diffMaiuscMinusc);
				}});
			}},BorderLayout.CENTER);
			getContentPane().add(new JPanel(){{//PAINEL BOTÕES
				setLayout(new GridLayout(1,2));
				add(new JButton("Procurar"){{
					addActionListener(new ActionListener(){
						public void actionPerformed(ActionEvent a){
							search(termo.getText(),frente.isSelected(),onlySelected.isSelected(),wholeWord.isSelected(),diffMaiuscMinusc.isSelected());
						}
					});
				}});
				add(new JButton("Fechar"){{
					addActionListener(new ActionListener(){
						public void actionPerformed(ActionEvent a){dispensar();}
					});}});
				setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
			}},BorderLayout.SOUTH);
			addWindowListener(new WindowListener(){
				public void windowOpened(WindowEvent w){}
				public void windowIconified(WindowEvent w){}
				public void windowDeiconified(WindowEvent w){}
				public void windowDeactivated(WindowEvent w){}
				public void windowClosing(WindowEvent w){dispensar();}
				public void windowClosed(WindowEvent w){}
				public void windowActivated(WindowEvent w){}
			});
		}};
		tree.getTexto().addObjetoSetListener(new ObjetoSetListener(){
			public void objetoModified(Objeto oldObj,Objeto newObj){reset();}
		});
	}
	public void chamar(){dialog.setVisible(true);}
	public void dispensar(){dialog.dispose();reset();}
	public void reset(){modIndex=-1;textIndex=-1;}
	private int modIndex=-1;
	private int textIndex=-1;
	private void search(String termo,boolean frente,boolean onlySelected,boolean wholeWord,boolean diffMaiuscMinusc){
		termo=(diffMaiuscMinusc?termo:termo.toLowerCase());
//		System.out.println(tree.getTexto().isFocusOwner());
//		if(tree.getTexto().isFocusOwner()){
//			for(String linha:tree.getTexto().getModulo().getTexto()){
//				linha=(diffMaiuscMinusc?linha:linha.toLowerCase());
//				final int index=indexTextoFound=(frente?linha.indexOf(termo,indexTextoFound+1):linha.lastIndexOf(termo,(indexTextoFound>0?indexTextoFound:0)));
//				if(index<0||wholeWord&&(linha.lastIndexOf(" ",index)!=index-1||linha.indexOf(" ",index)!=index+termo.length()))continue;
//				tree.getTexto().select(index,index+termo.length());
//				return;
//			}
//		}else{
			List<Modulo>mods=(onlySelected?tree.getSelectedObjetos().getModulos():tree.getObjetos().getModulos());
//			if(indexMod>=0&&mods.size()>1){
//				if(frente)mods=mods.subList(indexMod+1,mods.size());else mods=mods.subList(0,indexMod);
//			}
			for(int m=(modIndex>=0?modIndex+(frente?1:-1):0);m>=0&&m<mods.size();m+=(frente?1:-1))for(String linha:mods.get(m).getTexto()){
				linha=(diffMaiuscMinusc?linha:linha.toLowerCase());
				textIndex=(frente?linha.indexOf(termo,textIndex+1):linha.lastIndexOf(termo,textIndex));
				if(textIndex<0||wholeWord&&(linha.charAt(textIndex-1)!=' '||linha.charAt(textIndex+termo.length())!=' '))continue;
				tree.unSelectAllMods();
				tree.select(mods.get(m));
				modIndex=m;
				janelaTexto.requestFocus();
				tree.getTexto().select(textIndex,textIndex+termo.length()+1);
				return;
			}
//		}
		Toolkit.getDefaultToolkit().beep();
	}
}