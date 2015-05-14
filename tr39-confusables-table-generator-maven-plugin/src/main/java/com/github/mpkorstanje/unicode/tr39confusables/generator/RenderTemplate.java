package com.github.mpkorstanje.unicode.tr39confusables.generator;

import static com.github.mpkorstanje.unicode.tr39confusables.generator.Table.MixedScriptAnyCase;
import static com.github.mpkorstanje.unicode.tr39confusables.generator.Table.MixedScriptLowerCase;
import static com.github.mpkorstanje.unicode.tr39confusables.generator.Table.SingleScriptAnyCase;
import static com.github.mpkorstanje.unicode.tr39confusables.generator.Table.SingleScriptLowerCase;
import static com.google.common.collect.Collections2.filter;
import static java.util.Collections.sort;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.URL;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;

import com.google.common.base.Predicate;

final class RenderTemplate extends SimpleFileVisitor<Path> {

	private static final Comparator<Confusable> SOURCE_ASC = new Comparator<Confusable>() {
		@Override
		public int compare(Confusable o1, Confusable o2) {
			return Integer.compare(o1.source, o2.source);
		}
	};
	
	private static final class InTable implements Predicate<Confusable> {

		private final Table table;

		public InTable(Table table) {
			this.table = table;
		}

		@Override
		public boolean apply(Confusable input) {
			return table.equals(input.table);
		}
	}
	
	private static final String JAVA = ".java";
	private static final String STG = ".stg";
	
	private final TableModel mixedScriptAnyCase;

	private final TableModel mixedScriptLowerCase;

	private final Path output;

	private final TableModel singleScriptAnyCase;

	private final TableModel singleScriptLowerCase;

	private final Path source;
	private final URL table;
	private final List<String> header;

	public RenderTemplate(Path source, Path output,URL table, List<String> header,
			List<Confusable> confusables) {
		this.source = source;
		this.output = output;
		this.table = table;
		this.header = header;

		sort(confusables, SOURCE_ASC);

		{
			List<Confusable> filtered = new ArrayList<>(filter(confusables,
					new InTable(MixedScriptAnyCase)));
			mixedScriptAnyCase = new TableModel(filtered);
		}
		{
			List<Confusable> filtered = new ArrayList<>(filter(confusables,
					new InTable(MixedScriptLowerCase)));
			mixedScriptLowerCase = new TableModel(filtered);
		}
		{
			List<Confusable> filtered = new ArrayList<>(filter(confusables,
					new InTable(SingleScriptAnyCase)));
			singleScriptAnyCase = new TableModel(filtered);
		}
		{
			List<Confusable> filtered = new ArrayList<>(filter(confusables,
					new InTable(SingleScriptLowerCase)));
			singleScriptLowerCase = new TableModel(filtered);
		}

	}

	private ST createTemplate(Path file) {

		STGroup group = new STGroupFile(file.toString());
		ST template = group.getInstanceOf("confusables");

		
		template.add("url", table);
		template.add("date", new Date());
		template.add("header", header);
		template.add("mixedScriptAnyCase", mixedScriptAnyCase);
		template.add("mixedScriptLowerCase", mixedScriptLowerCase);
		template.add("singleScriptAnyCase", singleScriptAnyCase);
		template.add("singleScriptLowerCase", singleScriptLowerCase);

		return template;
	}

	@Override
	public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
			throws IOException {

		String sourceName = file.getFileName().toString();

		if (!sourceName.endsWith(STG)) {
			return FileVisitResult.CONTINUE;
		}

		Path relative = source.relativize(file.getParent());
		String ouputName = sourceName.substring(0,
				sourceName.lastIndexOf(STG))
				+ JAVA;

		Path outputPath = relative.resolve(new File(ouputName).toPath());
		File outputFile = output.resolve(outputPath).toFile();

		outputFile.getParentFile().mkdirs();

		FileOutputStream fos = new FileOutputStream(outputFile);
		OutputStreamWriter osw = new OutputStreamWriter(fos);

		try (PrintWriter bw = new PrintWriter(osw)) {
			bw.write(createTemplate(file).render());
		}

		return FileVisitResult.CONTINUE;
	}
}