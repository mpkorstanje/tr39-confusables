package com.github.mpkorstanje.unicode.tr39confusables;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;

import static java.lang.Integer.parseInt;
import static java.lang.Character.isValidCodePoint;

public class GenerateTables {

	public static void main(String[] args) throws IOException {

		new GenerateTables(
				new URL(
						"http://www.unicode.org/Public/security/latest/confusables.txt"),
				new File("src/main/tr39confusables"), new File(
						"target/generated-sources/tr39confusables/")).generate();

	}

	private static final class Confusable {

		public final int source;
		public final int[] target;

		public Confusable(int source, int[] target) {
			this.source = source;
			this.target = target;
		}

		@Override
		public String toString() {
			return "Confusable [source=" + source + ", target="
					+ Arrays.toString(target) + "]";
		}

	}

	private final List<Confusable> confusables = new ArrayList<>();
	private final List<String> header = new ArrayList<>();

	private static final Comparator<Confusable> SOURCE_ASC = new Comparator<Confusable>() {
		@Override
		public int compare(Confusable o1, Confusable o2) {
			return Integer.compare(o1.source, o2.source);
		}
	};

	private final URL table;
	private final File sourceDirectory;
	private final File outputDirectory;

	public GenerateTables(URL table, File sourceDirectory, File outputDirectory) {
		this.table = table;
		this.sourceDirectory = sourceDirectory;
		this.outputDirectory = outputDirectory;
	}

	public void generate() throws IOException {
		loadUnicodeData();
		makeTables();
	}

	private void loadUnicodeData() throws IOException {
		header.clear();
		confusables.clear();

		InputStream is = table.openStream();
		InputStreamReader in = new InputStreamReader(is, "UTF-8");
		try (BufferedReader reader = new BufferedReader(in)) {

			while (parseHeader(reader)) {
				// Proceed
			}

			while (parseLine(reader)) {
				// Proceed
			}
		}
	}

	private ST createTemplate(Path file) {
		Collections.sort(confusables, SOURCE_ASC);

		STGroup group = new STGroupFile(file.toString());
		ST template = group.getInstanceOf("confusables");

		List<Confusable> confusables01 = new ArrayList<>();
		List<Confusable> confusables02 = new ArrayList<>();
		Confusable pivot = confusables.get(confusables.size() / 2);
		for (Confusable c : confusables) {
			if (c.source < pivot.source) {
				confusables01.add(c);
			} else {
				confusables02.add(c);
			}
		}
		template.add("url", table);
		template.add("date", new Date());
		template.add("header", header);
		template.add("confusables01", confusables01);
		template.add("confusables02", confusables02);
		return template;
	}

	private void makeTables() throws IOException {
		final Path sourceDirectoryPath = sourceDirectory.toPath();
		final Path outputDirectoryPath = outputDirectory.toPath();

		Files.walkFileTree(sourceDirectoryPath, new SimpleFileVisitor<Path>() {

			@Override
			public FileVisitResult visitFile(Path file,
					BasicFileAttributes attrs) throws IOException {

				String sourceName = file.getFileName().toString();

				if (!sourceName.endsWith(".stg")) {
					return FileVisitResult.CONTINUE;
				}

				Path relative = sourceDirectoryPath.relativize(file.getParent());
				String ouputName = sourceName.substring(0,
						sourceName.lastIndexOf(".stg"))
						+ ".java";

				Path outputPath = relative.resolve(new File(ouputName)
						.toPath());
				File outputFile = outputDirectoryPath.resolve(outputPath)
						.toFile();

				outputFile.getParentFile().mkdirs();

				FileOutputStream fos = new FileOutputStream(outputFile);
				OutputStreamWriter osw = new OutputStreamWriter(fos);

				try (PrintWriter bw = new PrintWriter(osw)) {
					bw.write(createTemplate(file).render());
				}

				return FileVisitResult.CONTINUE;
			}
		});
	}

	private static int parseCodePoint(final String s) {
		int codePoint = parseInt(s, 16);

		if (!isValidCodePoint(codePoint)) {
			throw new IllegalArgumentException(s + " was not a valid codepoint");
		}

		return codePoint;
	}

	private boolean parseHeader(BufferedReader reader) throws IOException {
		String line = reader.readLine();

		if (line == null || line.isEmpty()) {
			return false;
		}

		if (line.startsWith("" + '\uFEFF')) {
			line = line.substring(1);
		}

		header.add(line);

		return true;
	}

	private boolean parseLine(BufferedReader reader) throws IOException {
		String line = reader.readLine();

		if (line == null) {
			return false;
		}

		if (line.isEmpty() || line.startsWith("#")) {
			return true;
		}

		// Each line in the data file has the following format: Field 1 is the
		// source, Field 2 is the target, and Field 3 is a type identifying the
		// table. Everything after the # is a comment and is purely informative.
		// A asterisk after the comment indicates that the character is not an
		// XID character [UAX31].
		final String[] fields = line.split("( ;\t)|(\t#)", 4);

		if (fields.length < 4) {
			throw new IllegalStateException(
					"Expected atleast 4 fields while parsing " + line);
		}
		// Mixed-Script, Any-Case This table is used to test cases of
		// mixed-script and whole-script confusables, where the output allows
		// for mixed case (which may be later folded away). For example, this
		// table contains the following entry not found in SL, SA, or ML:
		if (!fields[2].equals("MA")) {
			return true;
		}

		final int source = parseCodePoint(fields[0]);

		final String[] targetPoints = fields[1].split(" ");
		final int[] target = new int[targetPoints.length];
		for (int i = 0; i < target.length; i++) {
			target[i] = parseCodePoint(targetPoints[i]);
		}

		confusables.add(new Confusable(source, target));

		return true;
	}

}
