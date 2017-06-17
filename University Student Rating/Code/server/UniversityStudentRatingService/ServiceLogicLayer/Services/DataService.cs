using DataAccessLayer.DbContext;
using DataAccessLayer.Interfaces;
using ServiceLogicLayer.DTO;
using ServiceLogicLayer.Interfaces;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ServiceLogicLayer.Services
{
    public class DataService : IDataService
    {
        IUnitOfWork Database { get; set; }

        public DataService(IUnitOfWork uow)
        {
            Database = uow;
        }

        public IEnumerable<GenderType> GetGenderTypes()
        {
            return Database.GenderTypeRepository.GetAll();
        }

        public IEnumerable<Country> GetCountries()
        {
            return Database.CountryRepository.GetAll();
        }

        public IEnumerable<City> GetCitiesByCountry(int countryId)
        {
            return Database.CityRepository.Find(c => c.CountryId == countryId);
        }

        public IEnumerable<University> GetUniversitiesByCity(int cityId)
        {
            return Database.UniversityRepository.Find(u => u.CityId == cityId);
        }

        public IEnumerable<Status> GetStatuses()
        {
            return Database.StatusRepository.GetAll();
        }

        public IEnumerable<Faculty> GetFacultiesByUniversity(int universityId)
        {
            return Database.FacultyRepository.Find(u => u.UniversityId == universityId);
        }

        public IEnumerable<Course> GetCourses()
        {
            return Database.CourseRepository.GetAll();
        }

        public IEnumerable<ProfessorDiscipline> GetProfessorDisciplines(int branchId, int universityId)
        {
            return Database.ProfessorDisciplineRepository.Find(pd => pd.Discipline.BranchId == branchId && pd.UserProfile.UniversityId == universityId);
        }

        public IEnumerable<StudentProfessorDiscipline> GetStudentProfessorDisciplines(int studentId)
        {
            return Database.StudentProfessorDisciplineRepository.Find(spd => spd.StudentId == studentId);
        }

        public IEnumerable<ProfessorDiscipline> GetProfessorDisciplines(int professorId)
        {
            return Database.ProfessorDisciplineRepository.Find(p => p.ProfessorId == professorId);
        }

        public IEnumerable<Discipline> GetDisciplinesByProfessor(int professorId)
        {
            return Database.ProfessorDisciplineRepository.Find(p => p.ProfessorId == professorId).Select(pd => pd.Discipline);
        }

        public BranchProfessorDisciplineDTO GetBranchProfessorDiscipline(int studentId, int universityId)
        {
            BranchProfessorDisciplineDTO dto = new BranchProfessorDisciplineDTO();
            dto.sciProfessorDisciplines = GetProfessorDisciplines(2, universityId).ToList();
            dto.sportProfessorDisciplines = GetProfessorDisciplines(1, universityId).ToList();
            dto.artProfessorDisciplines = GetProfessorDisciplines(4, universityId).ToList();
            dto.societyProfessorDisciplines = GetProfessorDisciplines(5, universityId).ToList();
            dto.studentProfessorDisciplines = GetStudentProfessorDisciplines(studentId).ToList();

            return dto;
        }

        public void Dispose()
        {
            Database.Dispose();
        }


        public IEnumerable<RatingDivision> GetRatingDivisions()
        {
            return Database.RatingDivisionRepository.GetAll();
        }
    }
}
