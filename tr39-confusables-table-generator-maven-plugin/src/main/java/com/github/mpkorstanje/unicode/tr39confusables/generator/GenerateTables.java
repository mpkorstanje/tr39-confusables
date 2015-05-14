package com.github.mpkorstanje.unicode.tr39confusables.generator;

import static com.github.mpkorstanje.unicode.tr39confusables.generator.Table.parseTable;
import static java.lang.Character.isValidCodePoint;
import static java.lang.Integer.parseInt;
import static java.nio.file.Files.walkFileTree;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class GenerateTables {

	public static void main(String[] args) throws IOException {

		new GenerateTables(new URL(args[0]), new File(args[1]), new File(
				args[1])).generate();
	}

	private static int parseCodePoint(final String s) {
		int codePoint = parseInt(s, 16);

		if (!isValidCodePoint(codePoint)) {
			throw new IllegalArgumentException(s + " was not a valid codepoint");
		}

		return codePoint;
	}

	private final List<Confusable> confusables = new ArrayList<>();
	private final List<String> header = new ArrayList<>();
	private final File outputDirectory;
	private final File sourceDirectory;

	private final URL table;

	public GenerateTables(URL table, File sourceDirectory, File outputDirectory) {
		this.table = table;
		this.sourceDirectory = sourceDirectory;
		this.outputDirectory = outputDirectory;
	}

	public void generate() throws IOException {
		header.clear();
		confusables.clear();

		final InputStream is = table.openStream();
		final InputStreamReader in = new InputStreamReader(is, "UTF-8");
		try (BufferedReader reader = new BufferedReader(in)) {

			while (parseHeader(reader)) {
				// Proceed
			}

			while (parseLine(reader)) {
				// Proceed
			}
		}

		final Path source = sourceDirectory.toPath();
		final Path output = outputDirectory.toPath();

		walkFileTree(source, new RenderTemplate(source, output, table, header, confusables));
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

		final Table table = parseTable(fields[2]);
		final int source = parseCodePoint(fields[0]);

		final String[] targetPoints = fields[1].split(" ");
		final int[] target = new int[targetPoints.length];
		for (int i = 0; i < target.length; i++) {
			target[i] = parseCodePoint(targetPoints[i]);
		}

		confusables.add(new Confusable(table, source, target));

		return true;
	}

}
