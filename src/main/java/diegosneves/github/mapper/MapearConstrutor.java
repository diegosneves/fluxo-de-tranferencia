package diegosneves.github.mapper;

import diegosneves.github.exception.ConstrutorPadraoNaoDefinido;
import diegosneves.github.exception.MapearObjetoException;

import java.lang.reflect.Field;

/**
 * A interface MapearConstrutor fornece métodos estáticos para mapear objetos entre diferentes classes.
 */
public interface MapearConstrutor {

    /**
     * Constrói um novo objeto do tipo especificado {@code classeDeDestino} a partir de um objeto de origem {@code origem}.
     *
     * @param <T>             O tipo do objeto de destino.
     * @param classeDeDestino A classe do objeto de destino.
     * @param origem          O objeto de origem.
     * @return Um novo objeto do tipo {@code classeDeDestino} com os atributos mapeados a partir do objeto de origem.
     * @throws ConstrutorPadraoNaoDefinido Se um construtor padrão não está definido na classe do objeto de destino.
     * @throws MapearObjetoException      Se ocorrer um erro durante o mapeamento dos atributos.
     */
    static <T> T construirNovoDe(Class<T> classeDeDestino, Object origem) {

        var atributos = classeDeDestino.getDeclaredFields();

        T novaInstancia = null;

        try {
            novaInstancia = classeDeDestino.getConstructor().newInstance();
        } catch (NoSuchMethodException e) {
            throw new ConstrutorPadraoNaoDefinido(classeDeDestino.getName(), e);
        } catch (Exception e) {
            throw new MapearObjetoException(classeDeDestino.getName(), e);
        }

        for(Field atributo : atributos) {
            atributo.setAccessible(true);
            try {
                var atributoOrigem = origem.getClass().getDeclaredField(atributo.getName());
                atributoOrigem.setAccessible(true);
                atributo.set(novaInstancia, atributoOrigem.get(origem));
            } catch (Exception ignored) {
            }
        }

        return novaInstancia;

    }

    /**
     * Constrói um novo objeto do tipo especificado {@code classeDeDestino} a partir de um objeto de origem {@code origem}.<br>
     * Podendo conter uma estratégia.<br>
     *
     * @param <T>             O tipo do objeto de destino.
     * @param <E>             O tipo do objeto de origem.
     * @param classeDeDestino A classe do objeto de destino.
     * @param origem          O objeto de origem.
     * @param estrategia      A estratégia de mapeamento a ser utilizada.
     * @return Um novo objeto do tipo {@code classeDeDestino} com os atributos mapeados a partir do objeto de origem.
     */
    static <T, E> T construirNovoDe(Class<T> classeDeDestino, E origem, EstrategiaDeMapeamento<T, E> estrategia) {
        if (estrategia == null) {
            return construirNovoDe(classeDeDestino, origem);
        }
        return estrategia.mapear(origem);
    }

}
