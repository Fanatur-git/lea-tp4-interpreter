package lea;

import org.junit.jupiter.api.Test;
import lea.Reporter.Phase;

public final class ParserTest {

	/* =========================
	 * === SYNTAXE VALIDE
	 * ========================= */

	@Test
	void valid_simple_program() {
		new LeaAsserts("x <- 1; écrire(x);")
		.assertHasNoErrorAt(Phase.PARSER);
	}

	@Test
	void valid_expressions_complex() {
		new LeaAsserts("x <- 1 + 2 * 3 < 4 et vrai ou faux;")
		.assertHasNoErrorAt(Phase.PARSER);
	}

	@Test
	void valid_parentheses() {
		new LeaAsserts("x <- (1 + 2) * 3;")
		.assertHasNoErrorAt(Phase.PARSER);
	}

	@Test
	void valid_if_else() {
		new LeaAsserts("""
				si x < 0 alors
				    x <- x;
				sinon
				    x <- x;
				fin si
				""")
		.assertHasNoErrorAt(Phase.PARSER);
	}

	@Test
	void valid_empty_lines_and_comments() {
		new LeaAsserts("""
				// Commentaire
				x <- 1;
				// Autre
				écrire(x);
				""")
		.assertHasNoErrorAt(Phase.PARSER);
	}

	/* =========================
	 * === SYNTAXE INVALIDE (Erreurs attendues)
	 * ========================= */

	@Test
	void invalid_missing_semicolon() {
		new LeaAsserts("x <- 1 écrire(x);")
		.assertHasErrorContaining(Phase.PARSER, "");
	}

	@Test
	void invalid_unbalanced_parentheses() {
		new LeaAsserts("x <- (1 + 2;")
		.assertHasErrorContaining(Phase.PARSER, "");
	}

	@Test
	void invalid_incomplete_if() {
		new LeaAsserts("si vrai alors x <- 1; fin")
		.assertHasErrorContaining(Phase.PARSER, "");
	}

}