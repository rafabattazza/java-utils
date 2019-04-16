package info.agilite.utils;

public class TextFile {
	private StringBuilder builderText = new StringBuilder();
	private String separador = "";
	
	public void newLine(){
		builderText.append(Character.toChars(13));
		builderText.append(Character.toChars(10));
	}
	public void print(Object texto){
		print(texto, true);
	}
	public void print(Object texto, boolean removePulaLinha){
		if(texto==null)texto = "";
		if(removePulaLinha)texto = texto.toString().replace("\n", "");
		escrever(texto.toString());
	}
	
	public void print(Object texto, int tamanho){
		print(texto, tamanho, true);
	}
	public void print(Object texto, int tamanho, boolean removePulaLinha){
		if(texto==null)texto = "";
		if(texto instanceof String){
			if(removePulaLinha)texto = texto.toString().replace("\n", "");	
		}
		texto = StringUtils.ajustString(texto, tamanho);
		escrever(texto.toString());
	}
	
	public void print(Object texto, int tamanho, char caracter, boolean concatenarAEsquerda){
		print(texto, tamanho, caracter, concatenarAEsquerda, true);
	}
	public void print(Object texto, int tamanho, char caracter, boolean concatenarAEsquerda, boolean removePulaLinha){
		if(texto==null)texto = "";
		texto = StringUtils.ajustString(texto, tamanho, caracter, concatenarAEsquerda);
		if(removePulaLinha)texto = texto.toString().replace("\n", "");
		escrever(texto.toString());
	}
	
	public void printNull(int qtdSeparadores){
		for(int i = 0; i < qtdSeparadores; i++){
			escrever("");
		}
	}

	private void escrever(String texto){
		builderText.append(texto);
		if(separador != null)builderText.append(separador);
	}
	
	public String getText() {
		return builderText.toString();
	}
	public String getSeparador() {
		return separador;
	}
}
