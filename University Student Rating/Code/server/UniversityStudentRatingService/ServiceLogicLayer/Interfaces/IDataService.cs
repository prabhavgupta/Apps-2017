using DataAccessLayer.DbContext;
using ServiceLogicLayer.DTO;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ServiceLogicLayer.Interfaces
{
    public interface IDataService : IDisposable
    {
        IEnumerable<GenderType> GetGenderTypes();
        IEnumerable<Country> GetCountries();
        IEnumerable<City> GetCitiesByCountry(int countryId);
        IEnumerable<University> GetUniversitiesByCity(int cityId);
        IEnumerable<Status> GetStatuses();
        IEnumerable<Faculty> GetFacultiesByUniversity(int universityId);
        IEnumerable<Course> GetCourses();
        IEnumerable<ProfessorDiscipline> GetProfessorDisciplines(int branchId, int universityId);
        IEnumerable<ProfessorDiscipline> GetProfessorDisciplines(int professorId);
        IEnumerable<Discipline> GetDisciplinesByProfessor(int professorId);
        IEnumerable<StudentProfessorDiscipline> GetStudentProfessorDisciplines(int studentId);
        BranchProfessorDisciplineDTO GetBranchProfessorDiscipline(int studentId, int universityId);
        IEnumerable<RatingDivision> GetRatingDivisions();
    }
}