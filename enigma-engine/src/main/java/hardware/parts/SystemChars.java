package hardware.parts;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum SystemChars {
    SPACE(' '),
    EXCLAMATION('!'),
    DOUBLE_QUOTE('"'),
    HASH('#'),
    DOLLAR('$'),
    PERCENT('%'),
    AMPERSAND('&'),
    SINGLE_QUOTE('\''),
    LEFT_PAREN('('),
    RIGHT_PAREN(')'),
    ASTERISK('*'),
    PLUS('+'),
    COMMA(','),
    MINUS('-'),
    PERIOD('.'),
    FORWARD_SLASH('/'),
    ZERO('0'),
    ONE('1'),
    TWO('2'),
    THREE('3'),
    FOUR('4'),
    FIVE('5'),
    SIX('6'),
    SEVEN('7'),
    EIGHT('8'),
    NINE('9'),
    COLON(':'),
    SEMICOLON(';'),
    LESS_THAN('<'),
    EQUALS('='),
    GREATER_THAN('>'),
    QUESTION('?'),
    AT('@'),
    A('A'),
    B('B'),
    C('C'),
    D('D'),
    E('E'),
    F('F'),
    G('G'),
    H('H'),
    I('I'),
    J('J'),
    K('K'),
    L('L'),
    M('M'),
    N('N'),
    O('O'),
    P('P'),
    Q('Q'),
    R('R'),
    S('S'),
    T('T'),
    U('U'),
    V('V'),
    W('W'),
    X('X'),
    Y('Y'),
    Z('Z'),
    LEFT_BRACKET('['),
    BACK_SLASH('\\'),
    RIGHT_BRACKET(']'),
    CARET('^'),
    UNDERSCORE('_'),
    BACKTICK('`'),
    a('a'),
    b('b'),
    c('c'),
    d('d'),
    e('e'),
    f('f'),
    g('g'),
    h('h'),
    i('i'),
    j('j'),
    k('k'),
    l('l'),
    m('m'),
    n('n'),
    o('o'),
    p('p'),
    q('q'),
    r('r'),
    s('s'),
    t('t'),
    u('u'),
    v('v'),
    w('w'),
    x('x'),
    y('y'),
    z('z'),
    LEFT_BRACE('{'),
    PIPE('|'),
    RIGHT_BRACE('}'),
    TILDE('~');

    private final char value;

    SystemChars(char value) {
        this.value = value;
    }

    public char getValue() {
        return value;
    }

    public Boolean ValidateChars(String charsIWant) {
        Set<Character> validChars = Stream.of(SystemChars.values())
                .map(SystemChars::getValue)
                .collect(Collectors.toSet());

        for (char c : charsIWant.toCharArray()) {
            if (!validChars.contains(c)) {
                return false;
            }
        }
        return true;
    }
}
