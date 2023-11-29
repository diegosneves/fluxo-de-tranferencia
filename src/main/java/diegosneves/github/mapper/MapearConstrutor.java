package diegosneves.github.mapper;

import diegosneves.github.exception.ConstrutorPadraoNaoDefinido;
import diegosneves.github.exception.MapearObjetoException;

import java.lang.reflect.Field;

public interface MapearConstrutor {

    static <T> T construirNovoDe(Class<T> classeDeDestino, Object origem) {

        var atributos = classeDeDestino.getDeclaredFields();

        T novaInstancia = null;

        try {
            novaInstancia = classeDeDestino.getConstructor().newInstance();
        } catch (NoSuchMethodException e) {
            throw new ConstrutorPadraoNaoDefinido(classeDeDestino.getName());
        } catch (Exception e) {
            throw new MapearObjetoException(classeDeDestino.getName());
        }

        for(Field atributo : atributos) {
            atributo.setAccessible(true);
            try {
                var atributoOrigem = origem.getClass().getDeclaredField(atributo.getName());
                atributoOrigem.setAccessible(true);
                atributo.set(novaInstancia, atributoOrigem.get(origem));
            } catch (Exception e) {
            }
        }

        return novaInstancia;

    }

    static <T, E> T construirNovoDe(Class<T> classeDeDestino, E origem, EstrategiaDeMapeamento<T, E> estrategia) {
        if (estrategia == null) {
            return construirNovoDe(classeDeDestino, origem);
        }
        return estrategia.mapear(origem);
    }

}
