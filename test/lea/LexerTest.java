package lea;

import org.junit.jupiter.api.Test;

import lea.Reporter.Phase;

/**
 * JUnit tests for the Lexer class.
 */
public final class LexerTest {

	/* =========================
	 * === IDENTIFIANTS / MOTS-CLES
	 * ========================= */
	
	@Test
	void identifier_basic() {
	    new LeaAsserts("x")
			.assertMatches(Terminal.ID);
	}

	@Test
    void identifier_with_underscores_and_digits() {
        new LeaAsserts("a_1_b2")
            .assertMatches(Terminal.ID);
    }

    @Test
    void keyword_vs_identifier_prefix() {
        new LeaAsserts("si sinon simon")
            .assertMatches(Terminal.SI, Terminal.SINON, Terminal.ID);
    }

    @Test
    void keywords_allCore() {
        new LeaAsserts("si alors sinon fin écrire")
            .assertMatches(Terminal.SI, Terminal.ALORS, Terminal.SINON, Terminal.FIN, Terminal.ECRIRE);
    }

    /* =========================
     * === SYMBOLES / OPERATEURS
     * ========================= */

    @Test
    void punctuation_and_assignment() {
        new LeaAsserts("<- ; ( )")
            .assertMatches(Terminal.AFFECTATION, Terminal.PT_VIRG, Terminal.PAR_G, Terminal.PAR_D);
    }

    @Test
    void operators_and_logicals() {
        new LeaAsserts("+ - * = < et ou")
            .assertMatches(
                Terminal.PLUS, Terminal.MOINS, Terminal.MULTIPLIE,
                Terminal.EGAL, Terminal.INFERIEUR,
                Terminal.ET, Terminal.OU
            );
    }

    /* =========================
     * === LITTERAUX
     * ========================= */

    @Test
    void boolean_literals() {
        new LeaAsserts("vrai faux")
            .assertMatches(Terminal.LITERAL, Terminal.LITERAL);
    }

    @Test
    void integer_literals() {
        new LeaAsserts("0 7 42 123456")
            .assertMatches(Terminal.LITERAL, Terminal.LITERAL, Terminal.LITERAL, Terminal.LITERAL);
    }

    /* =========================
     * === ESPACES / COMMENTAIRES
     * ========================= */

    @Test
    void whitespace_is_ignored() {
        new LeaAsserts(" \n\t  x \r\f y ")
            .assertMatches(Terminal.ID, Terminal.ID);
    }

    @Test
    void line_comment_is_ignored() {
        new LeaAsserts("""
                x // comment
                y
                """)
            .assertMatches(Terminal.ID, Terminal.ID);
    }

    @Test
    void block_comment_is_ignored() {
        new LeaAsserts("x /* comment */ y")
            .assertMatches(Terminal.ID, Terminal.ID);
    }

    @Test
    void block_comment_with_stars_is_ignored() {
        new LeaAsserts("x /* ** */ y")
            .assertMatches(Terminal.ID, Terminal.ID);
    }

    /* =========================
     * === ERREURS LEXICALES
     * ========================= */

    @Test
    void illegal_character_is_reported() {
        new LeaAsserts("@")
            .assertHasErrorContaining(Phase.LEXER, "Illegal character");
    }

    @Test
    void illegal_character_does_not_prevent_other_tokens() {
        new LeaAsserts("x @ y")
            .assertMatches(Terminal.ID, Terminal.ID);
    }

    @Test
    void unexpected_dot_is_illegal() {
        new LeaAsserts(".")
            .assertHasErrorContaining(Phase.LEXER, "Illegal character");
    }
    
}
