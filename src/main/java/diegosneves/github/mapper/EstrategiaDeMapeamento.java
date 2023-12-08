package diegosneves.github.mapper;

/**
 * A interface EstrategiaDeMapeamento define uma estratégia de mapeamento para mapear objetos do tipo {@link Object E} para o tipo {@link Object T}.
 *
 * @param <T> O tipo do objeto de destino.
 * @param <E> O tipo do objeto de origem.
 */
public interface EstrategiaDeMapeamento <T, E> {

    /**
     * Mapeia um objeto do tipo {@link Object E} para o tipo {@link Object T}.
     *
     * @param origem O objeto a ser mapeado.
     * @return O objeto mapeado do tipo {@link Object T}.
     */
    T mapear(E origem);

}
