package com.excilys.cdb.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.persistence.CompanyDao;
import com.excilys.cdb.persistence.ComputerDao;
import com.excilys.cdb.persistence.DaoFactory;
import com.excilys.cdb.persistence.Page;
import com.excilys.cdb.persistence.exceptions.CompanyNotFoundException;
import com.excilys.cdb.persistence.exceptions.ComputerNotFoundException;
import com.excilys.cdb.persistence.exceptions.DAOConfigurationException;
import com.excilys.cdb.persistence.exceptions.DaoException;
import com.excilys.cdb.service.exceptions.DateInvalidException;
import com.excilys.cdb.service.exceptions.NameInvalidException;
import com.excilys.cdb.service.exceptions.ServiceException;
import com.excilys.cdb.validator.CompanyValidator;
import com.excilys.cdb.validator.ComputerValidator;
import com.excilys.cdb.validator.TextValidation;
import com.excilys.cdb.validator.exceptions.ValidatorDateException;
import com.excilys.cdb.validator.exceptions.ValidatorStringException;

/**
 * Service permettant de gérer les requêtes de gestion de la table computer et
 * de la table company qui lui est lié.
 * @author vogel
 *
 */
public class ComputerService {

    private ComputerDao computerDao;
    private CompanyDao companyDao;

    private static ComputerService service;

    /**
     * Récupérer une instance de type computerServiceImpl.
     * @return une référence sur le singleton ComputerServiceImpl.
     * @throws DAOConfigurationException erreur de configuration
     */
    public static ComputerService getInstance()
            throws DAOConfigurationException {
        if (service == null) {
            Logger logger = LoggerFactory.getLogger(ComputerService.class);
            logger.info("Initialisation du singleton computer service");

            service = new ComputerService();
            DaoFactory factory = DaoFactory.getInstance();
            service.computerDao = (ComputerDao) factory
                    .getDao(DaoFactory.DaoType.COMPUTER);
            service.companyDao = (CompanyDao) factory
                    .getDao(DaoFactory.DaoType.COMPANY);
        }
        return service;
    }

    /**
     * Retourne tous les computers de la bdd.
     * @return une liste
     * @throws ServiceException erreur de paramètres
     */
    public List<Computer> getAllComputer() throws ServiceException {
        List<Computer> list = new ArrayList<Computer>();
        try {
            list = computerDao.getAll();
        } catch (DaoException e) {
            throw new ServiceException("Méthode dao fail", e);
        }
        return list;
    }

    /**
     * Retourne toutes les company de la bdd.
     * @return une liste
     * @throws ServiceException erreur de paramètres
     */
    public List<Company> getAllCompany() throws ServiceException {
        List<Company> list = new ArrayList<Company>();
        try {
            list = companyDao.getAll();
        } catch (DaoException e) {
            throw new ServiceException("Méthode dao fail", e);
        }
        return list;
    }

    /**
     * Retourne toutes les companies comprenant name.
     * @param name le nom de la(les) company(ies).
     * @return une liste de company
     * @throws ServiceException erreur de paramètres
     * @throws DaoException erreur de reqûete.
     */
    public List<Company> getCompanyByName(String name) throws ServiceException, DaoException {
        String nameTraiter = TextValidation.traitementString(name);
        return companyDao.getByName(nameTraiter);
    }

    /**
     * Retourne tous les computer de nom : name.
     * @param name le nom du(des) computer(s).
     * @return une liste de computers
     * @throws ServiceException erreur de paramètres
     * @throws DaoException erreur de reqûete.
     */
    public List<Computer> getComputerByName(String name) throws ServiceException, DaoException {
        String nameTraiter = TextValidation.traitementString(name);
        return computerDao.getByName(nameTraiter);
    }

    /**
     * Optenir une page de company.
     * @param page le numero de la page
     * @return une page chargé.
     * @throws ServiceException erreur de service
     */
    public Page<Company> getPageCompany(final int page) throws ServiceException {
        return getPageCompany(page, null);
    }

    /**
     * Optenir une page de company avec un nombre d'éléments spécifié.
     * @param page le numero de la page
     * @param limit le nombre d'objets à instancié, if null: valeur default.
     * @return une page chargé.
     * @throws ServiceException erreur de paramètres
     */
    public Page<Company> getPageCompany(final int page, final Integer limit)
            throws ServiceException {
        Page<Company> pageCompany = null;
        try {
            if (limit == null) {
                pageCompany = companyDao.getPage(page);
            } else {
                pageCompany = companyDao.getPage(page, limit);
            }
        } catch (DaoException e) {
            throw new ServiceException("Méthode dao fail", e);
        }
        return pageCompany;
    }

    /**
     * Optenir une page de computer.
     * @param page le numero de la page
     * @return une page chargé.
     * @throws ServiceException erreur de paramètres
     */
    public Page<Computer> getPageComputer(final int page) throws ServiceException {
        return getPageComputer(page, null);
    }

    /**
     * Optenir une page de computer avec un nombre d'éléments spécifié.
     * @param page le numero de la page
     * @param limit le nombre d'éléments à récupérer, si null: valeur default.
     * @return une page chargé.
     * @throws ServiceException erreur de paramètres
     */
    public Page<Computer> getPageComputer(final int page, final Integer limit) throws ServiceException {
        Page<Computer> pageComputer = null;
        try {
            if (limit == null) {
                pageComputer = computerDao.getPage(page);
            } else {
                pageComputer = computerDao.getPage(page, limit);
            }
        } catch (DaoException e) {
            throw new ServiceException("Méthode dao fail", e);
        }
        return pageComputer;
    }

    /**
     * Recherche de computer par nom.
     * @param search le nom a chercher
     * @param page le numero de la page
     * @param limit le nombre d'éléments à récupérer, si null: valeur default.
     * @return une page chargé.
     * @throws ServiceException erreur de paramètres
     */
    public Page<Computer> getPageSearchComputer(final String search, final int page, final Integer limit) throws ServiceException {
        Page<Computer> pageComputer = null;
        try {
            if (limit == null) {
                pageComputer = computerDao.getPageSearch(search, page);
            } else {
                pageComputer = computerDao.getPageSearch(search, page, limit);
            }
        } catch (DaoException e) {
            throw new ServiceException("Méthode dao fail", e);
        }
        return pageComputer;
    }

    /**
     * Recherche de compagnie par nom.
     * @param search le nom à chercher.
     * @param page le numero de la page
     * @param limit le nombre d'éléments à récupérer, si null: valeur default.
     * @return une page chargé.
     * @throws ServiceException erreur de paramètres
     */
    public Page<Company> getPageSearchCompany(final String search, final int page, final Integer limit) throws ServiceException {
        Page<Company> pageComputer = new Page<Company>(0, 0);
        String searchTraiter = TextValidation.traitementString(search);
        if (search != null) {
            try {
                if (limit == null) {
                    pageComputer = companyDao.getPageSearch(searchTraiter, page);
                } else {
                    pageComputer = companyDao.getPageSearch(searchTraiter, page, limit);
                }
            } catch (DaoException e) {
                throw new ServiceException("Méthode dao fail", e);
            }
        }
        return pageComputer;
    }

    /**
     * Retourne une company de la bdd.
     * @param id l'id de l'élément à récupérer
     * @return la Company résultant
     * @throws ServiceException erreur de paramètres
     * @throws DaoException erreur de reqûete.
     */
    public Company getCompany(long id) throws ServiceException, DaoException {
        Company company = companyDao.getById(id).orElseThrow(() -> new CompanyNotFoundException(id));
        return company;
    }

    /**
     * Retourne un computer de la bdd.
     * @param id l'id de l'élément à récupérer
     * @return le Computer résultant
     * @throws ServiceException erreur de paramètres
     * @throws DaoException erreur de reqûete.
     */
    public Computer getComputer(long id) throws ServiceException, DaoException {
        Computer computer = computerDao.getById(id).orElseThrow(() -> new ComputerNotFoundException(id));
        return computer;
    }

    /** Créer un computer.
     * @param name le nom du computer
     * @return boolean le résultat
     * @throws ServiceException erreur de paramètres.
     * @throws DaoException erreur de requête.
     */
    public long createComputer(final String name) throws ServiceException, DaoException {
        long result = -1;

        String nameTraiter = TextValidation.traitementString(name);
        try {
            ComputerValidator.validName(nameTraiter);
            Computer c = new Computer(nameTraiter);
            result = computerDao.create(c);
        } catch (ValidatorStringException e) {
            throw new NameInvalidException(nameTraiter, e.getReason());
        }
        return result;
    }

    /** Créer une company.
     * @param name le nom de la compagnie
     * @return boolean le résultat
     * @throws ServiceException erreur de paramètres.
     * @throws DaoException erreur de requête.
     */
    public long createCompany(final String name) throws ServiceException, DaoException {
        long result = -1;

        String nameTraiter = TextValidation.traitementString(name);
        try {
            CompanyValidator.validName(nameTraiter);
            Company c = new Company(nameTraiter);
            result = companyDao.create(c);
        } catch (ValidatorStringException e) {
            throw new NameInvalidException(nameTraiter, e.getReason());
        }
        return result;
    }


    /** Créer un computer.
     * @param name le nom de la compagnie ou null
     * @param introduced la date ou null
     * @param discontinued la date d'arret ou null
     * @param companyId l'id de la company ou -1.
     * @return boolean le résultat
     * @throws ServiceException erreur de paramètres.
     * @throws DaoException erreur de requête.
     */
    public long createComputer(String name, LocalDateTime introduced,
            LocalDateTime discontinued, long companyId)
            throws ServiceException, DaoException {
        long result = -1;
        try {
            String nameTraiter = TextValidation.traitementString(name);
            Company inter = null;

            if (companyId > 0) {
                inter = companyDao.getById(
                        companyId).orElseThrow(() -> new CompanyNotFoundException(companyId));
            }

            Computer c = new Computer(nameTraiter, introduced, discontinued, inter);
            ComputerValidator.validComputer(c);
            result = computerDao.create(c);

        } catch (ValidatorDateException e) {
            throw new DateInvalidException(e.getReason());
        } catch (ValidatorStringException e) {
            throw new NameInvalidException(name, e.getReason());
        }

        return result;
    }

    /** Détruire un computer.
     * @param id l'id du computer à supprimer
     * @return un boolean représentant le résultat
     * @throws DaoException erreur de requête
     */
    public boolean deleteComputer(long id) throws DaoException {
        if (id < 1) {
            return false;
        }
        return computerDao.delete(id);
    }

    /** Détruire une compagnie.
     * @param id l'id du computer à supprimer
     * @return un boolean représentant le résultat
     * @throws DaoException erreur de requête
     */
    public boolean deleteCompany(long id) throws DaoException {
        if (id < 1) {
            return false;
        }
        return companyDao.delete(id);
    }

    /** Détruire plusieurs compagnies.
     * @param ids id(s) des compagnies à supprimer
     * @return un boolean représentant le résultat
     * @throws DaoException erreur de requête
     */
    public boolean deleteCompanies(Set<Long> ids) throws DaoException {
        if (ids == null || ids.size() == 0) {
            return false;
        }
        return companyDao.delete(ids);
    }

    /** Détruire plusieurs computers.
     * @param ids id(s) des computers à supprimer
     * @return un boolean représentant le résultat
     * @throws DaoException erreur de requête
     */
    public boolean deleteComputers(Set<Long> ids) throws DaoException {
        if (ids == null || ids.size() == 0) {
            return false;
        }
        return computerDao.delete(ids);
    }

    /** Modifié un computer.
     * @param id l'id du computer a modifié
     * @param name le nouveau nom du computer
     * @return boolean le résultat
     * @throws DaoException erreur de reqûete.
     * @throws ServiceException erreur de paramètres.
     */
    public boolean updateComputer(long id, String name) throws ServiceException, DaoException {
        Computer c;
        String nameTraiter = TextValidation.traitementString(name);
        try {
            ComputerValidator.validName(nameTraiter);
            c = computerDao.getById(id).orElseThrow(() -> new ComputerNotFoundException(id));
            c.setName(nameTraiter);
        } catch (ValidatorStringException e) {
            throw new NameInvalidException(nameTraiter, e.getReason());
        }
        return computerDao.update(c);
    }

    /** Modifié un computer.
     * @param id l'id du computer à modifié.
     * @param name le nouveau nom de la compagnie ou null
     * @param introduced la nouvelle date ou null
     * @param discontinued la nouvelle date d'arret ou null
     * @param companyId l'id de la nouvelle company, 0 pour supprimer, ou -1 pour null.
     * @return un boolean pour le résultat
     * @throws ServiceException erreur de paramètres
     * @throws DaoException erreur de reqûete.
     */
    public boolean updateComputer(long id, String name,
            LocalDateTime introduced, LocalDateTime discontinued,
            long companyId) throws ServiceException, DaoException {
        if (name == null && introduced == null
                && discontinued == null && companyId == -1) {
            throw new ServiceException("Aucun éléments n'a été spécifié.");
        }

        String nameTraiter = TextValidation.traitementString(name);
        Computer nouveau = new Computer();
        Computer initial = computerDao.getById(id).orElseThrow(() -> new ComputerNotFoundException(id));
        nouveau.setId(initial.getId());
        nouveau.setName(nameTraiter == null ? initial.getName() : nameTraiter);
        nouveau.setIntroduced(introduced == null ? initial.getIntroduced() : introduced);
        nouveau.setDiscontinued(discontinued == null ? initial.getDiscontinued() : discontinued);
        if (companyId == -1) {
            nouveau.setCompany(initial.getCompany());
        } else if (companyId == 0) {
            nouveau.setCompany(null);
        } else {
            Company comp = companyDao.getById(companyId).orElseThrow(() -> new CompanyNotFoundException(companyId));
            nouveau.setCompany(comp);
        }
        try {
            ComputerValidator.validComputer(nouveau);
        } catch (ValidatorStringException e) {
            throw new NameInvalidException(nameTraiter, e.getReason());
        } catch (ValidatorDateException e) {
            throw new DateInvalidException(e.getReason());
        }

        return computerDao.update(nouveau);
    }

    /** Récupérer le nombre de computers.
     * @return un type long
     * @throws ServiceException erreur de service.
     * @throws DaoException erreur de requête.
     */
    public long countComputers() throws ServiceException, DaoException {
        return computerDao.getCount();
    }

    /** Récupérer le nombre de compagnies existantes.
     * @return un type long
     * @throws ServiceException erreur de service.
     * @throws DaoException erreur de requête.
     */
    public long countCompanies() throws ServiceException, DaoException {
        return companyDao.getCount();
    }
}