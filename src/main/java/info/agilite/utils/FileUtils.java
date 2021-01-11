package info.agilite.utils;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;

import org.apache.commons.io.IOUtils;



public class FileUtils {
	public static void deleteRecursive(File dir) throws IOException {
		Files.walk(dir.toPath()).sorted(Comparator.reverseOrder()).map(Path::toFile).forEach(File::delete);
	}

	public static void createDirAndFile(File file) throws IOException {
		if (!file.getParentFile().exists())
			file.getParentFile().mkdirs();
		file.createNewFile();
	}

	public static void override(String fileName, String data) throws IOException {
		FileUtils.override(new File(fileName), data);
	}
	
	public static void override(File file, String data) throws IOException {
		override(file, data.getBytes("UTF-8"));
	}
	
	public static void override(File file, byte[] data) throws IOException {
		if(file.exists())file.delete();
		if(file.getParentFile() != null && !file.getParentFile().exists())file.getParentFile().mkdirs();
		file.createNewFile();
		try(FileOutputStream os = new FileOutputStream(file)){
			IOUtils.write(data, os);
		}
	}
	
	public static String streamToString(InputStream is) throws IOException {
		return streamToString(is, "UTF-8");
	}
	
	public static String streamToString(InputStream is, String encoding) throws IOException {
		try(StringWriter wr = new StringWriter()){
			IOUtils.copy(is, wr, encoding);
			return wr.toString();
		}
	}
	
	public static void createRootFolderFileValidate() {
		File f = new File("./test.dat");
		try {
			validarAcessoAoDiretorio(f);
			
		} catch (IOException e) {
			throw new RuntimeException("Seu usuário do sistema operacional não tem permissão para criar/deletar arquivos na pasta de instalação do programa");
		}
	}
	
	public static void validarAcessoCompletoAoDiretorio(File diretorio) {
		File f = new File(diretorio, "./test.dat");
		String path = null;
		try {
			path = diretorio.getCanonicalPath();
			diretorio.mkdirs();
			validarAcessoAoDiretorio(f);
		} catch (IOException e) {
			throw new RuntimeException("Seu usuário do sistema operacional não tem permissão para criar/deletar arquivos na pasta " + path);
		}
	}

	private static void validarAcessoAoDiretorio(File f) throws IOException {
		if(f.exists()) {
			boolean deleted = f.delete();
			if(!deleted)throw new RuntimeException("Arquivo de teste não pode ser deletado");
		}
		boolean created = f.createNewFile();
		if(!created)throw new RuntimeException("Arquivo de teste não foi criado");
		
		if(!f.canRead() || !f.canWrite())throw new RuntimeException("Sem permissão de acesso ao arquivo");
		
		boolean deleted = f.delete();
		if(!deleted)throw new RuntimeException("Arquivo de teste não pode ser deletado");
	}
}
