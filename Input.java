import java.awt.event.KeyEvent;

public class Input {

    int frameC = 0;

    public void frame() {
        frameC++;
    }

    public enum KeyState {
        W, A, S, D, SHIFT, SPACE, TAB, ESC, Q, E
    }

    public Input() {
        for (int i = 0; i < keys.length; i++) {
            keys[i] = new Key();
            keys[i].pressed = false;
            keys[i].eventTime = -1;
        }
    }

    public boolean pressed(KeyState key) {
        return keys[key.ordinal()].pressed;
    }

    public boolean justPressed(KeyState key) {
        return keys[key.ordinal()].pressed && keys[key.ordinal()].eventTime == frameC-1;
    }

    public boolean justReleased(KeyState key) {
        return !keys[key.ordinal()].pressed && keys[key.ordinal()].eventTime == frameC-1;
    }

    Key[] keys = new Key[KeyState.values().length];

    public void KeyPressed(KeyEvent evt) {
        // Check if the key is one of the keys we care about
        switch (evt.getKeyCode()) {
            case KeyEvent.VK_W:
                keys[KeyState.W.ordinal()].pressed = true;
                keys[KeyState.W.ordinal()].eventTime = frameC;
                break;
            case KeyEvent.VK_A:
                keys[KeyState.A.ordinal()].pressed = true;
                keys[KeyState.A.ordinal()].eventTime = frameC;
                break;
            case KeyEvent.VK_S:
                keys[KeyState.S.ordinal()].pressed = true;
                keys[KeyState.S.ordinal()].eventTime = frameC;
                break;
            case KeyEvent.VK_D:
                keys[KeyState.D.ordinal()].pressed = true;
                keys[KeyState.D.ordinal()].eventTime = frameC;
                break;
            case KeyEvent.VK_SHIFT:
                keys[KeyState.SHIFT.ordinal()].pressed = true;
                keys[KeyState.SHIFT.ordinal()].eventTime = frameC;
                break;
            case KeyEvent.VK_SPACE:
                keys[KeyState.SPACE.ordinal()].pressed = true;
                keys[KeyState.SPACE.ordinal()].eventTime = frameC;
                break;
            case KeyEvent.VK_TAB:
                keys[KeyState.TAB.ordinal()].pressed = true;
                keys[KeyState.TAB.ordinal()].eventTime = frameC;
                break;
            case KeyEvent.VK_ESCAPE:
                keys[KeyState.ESC.ordinal()].pressed = true;
                keys[KeyState.ESC.ordinal()].eventTime = frameC;
                break;
            case KeyEvent.VK_Q:
                keys[KeyState.Q.ordinal()].pressed = true;
                keys[KeyState.Q.ordinal()].eventTime = frameC;
                break;
            case KeyEvent.VK_E:
                keys[KeyState.E.ordinal()].pressed = true;
                keys[KeyState.E.ordinal()].eventTime = frameC;
                break;
        }
    }

    public void KeyReleased(KeyEvent evt) {
        switch (evt.getKeyCode()) {
            case KeyEvent.VK_W:
                keys[KeyState.W.ordinal()].pressed = false;
                keys[KeyState.W.ordinal()].eventTime = frameC;
                break;
            case KeyEvent.VK_A:
                keys[KeyState.A.ordinal()].pressed = false;
                keys[KeyState.A.ordinal()].eventTime = frameC;
                break;
            case KeyEvent.VK_S:
                keys[KeyState.S.ordinal()].pressed = false;
                keys[KeyState.S.ordinal()].eventTime = frameC;
                break;
            case KeyEvent.VK_D:
                keys[KeyState.D.ordinal()].pressed = false;
                keys[KeyState.D.ordinal()].eventTime = frameC;
                break;
            case KeyEvent.VK_SHIFT:
                keys[KeyState.SHIFT.ordinal()].pressed = false;
                keys[KeyState.SHIFT.ordinal()].eventTime = frameC;
                break;
            case KeyEvent.VK_SPACE:
                keys[KeyState.SPACE.ordinal()].pressed = false;
                keys[KeyState.SPACE.ordinal()].eventTime = frameC;
                break;
            case KeyEvent.VK_TAB:
                keys[KeyState.TAB.ordinal()].pressed = false;
                keys[KeyState.TAB.ordinal()].eventTime = frameC;
                break;
            case KeyEvent.VK_ESCAPE:
                keys[KeyState.ESC.ordinal()].pressed = false;
                keys[KeyState.ESC.ordinal()].eventTime = frameC;
                break;
            case KeyEvent.VK_Q:
                keys[KeyState.Q.ordinal()].pressed = false;
                keys[KeyState.Q.ordinal()].eventTime = frameC;
                break;
            case KeyEvent.VK_E:
                keys[KeyState.E.ordinal()].pressed = false;
                keys[KeyState.E.ordinal()].eventTime = frameC;
                break;
        }
    }

}