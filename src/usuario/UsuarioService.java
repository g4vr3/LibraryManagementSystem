package usuario;

import exception.ServiceException;

import java.util.List;

/**
 * Service class for managing users (usuarios).
 * Provides methods for creating, reading, updating, and deleting users.
 *
 * @version 1.0
 */
public class UsuarioService {
    private List<DTOUsuario> usuariosInMemory;
    private DAOUsuario daoUsuario;

    /**
     * Initializes a new instance of UsuarioService.
     * Loads all users from the data source into memory.
     *
     * @throws ServiceException if there is an error while reading users from the data source
     */
    public UsuarioService() throws ServiceException {
        daoUsuario = new DAOUsuario();
        usuariosInMemory = daoUsuario.readAll();
    }

    /**
     * Creates a new user with the specified name.
     *
     * @param nombre the name of the user
     * @throws ServiceException if there is an error while creating the user
     */
    public void createUsuario(String nombre) throws ServiceException {
        DTOUsuario dtoUsuario = new DTOUsuario(nombre);
        daoUsuario.create(dtoUsuario);
        usuariosInMemory.add(dtoUsuario);
    }

    /**
     * Reads a user by their ID.
     *
     * @param id the ID of the user to be read
     * @return the user DTO
     * @throws ServiceException if the user does not exist
     */
    public DTOUsuario readUsuario(Integer id) throws ServiceException {
        return daoUsuario.read(id);
    }

    /**
     * Updates an existing user with a new name.
     *
     * @param id the ID of the user to be updated
     * @param nombre the new name for the user
     * @throws ServiceException if the user does not exist
     */
    public void updateUsuario(Integer id, String nombre) throws ServiceException {
        DTOUsuario dtoUsuario = findUsuarioById(id);
        if (dtoUsuario != null) {
            dtoUsuario.setNombre(nombre);
            daoUsuario.update(dtoUsuario);
        } else {
            throw new ServiceException("El usuario que intentas actualizar no existe");
        }
    }

    /**
     * Deletes a user by their ID.
     *
     * @param id the ID of the user to be deleted
     * @throws ServiceException if the user does not exist
     */
    public void deleteUsuario(Integer id) throws ServiceException {
        DTOUsuario dtoUsuario = findUsuarioById(id);
        if (dtoUsuario != null) {
            daoUsuario.delete(dtoUsuario);
            usuariosInMemory.remove(dtoUsuario);
        } else {
            throw new ServiceException("El usuario que intentas eliminar no existe");
        }
    }

    /**
     * Finds a user by their ID.
     *
     * @param id the ID of the user to be found
     * @return the user DTO
     * @throws ServiceException if the user is not found
     */
    public DTOUsuario findUsuarioById(Integer id) throws ServiceException {
        for (DTOUsuario dtoUsuario : usuariosInMemory) {
            if (dtoUsuario.getId().equals(id)) {
                return dtoUsuario;
            }
        }
        throw new ServiceException("Usuario no encontrado");
    }
}
