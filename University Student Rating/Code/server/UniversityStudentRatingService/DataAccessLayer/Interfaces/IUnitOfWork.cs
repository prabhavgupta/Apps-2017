using DataAccessLayer.DbContext;
using DataAccessLayer.Identity;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace DataAccessLayer.Interfaces
{
    public interface IUnitOfWork : IDisposable
    {
        ApplicationUserManager ApplicationUserManager { get; }
        ApplicationRoleManager ApplicationRoleManager { get; }
        IGenericRepository<UserProfile> UserRepository { get; }
        IGenericRepository<GenderType> GenderTypeRepository { get; }
        IGenericRepository<Country> CountryRepository { get; }
        IGenericRepository<City> CityRepository { get; }
        IGenericRepository<University> UniversityRepository { get; }
        IGenericRepository<Status> StatusRepository { get; }
        IGenericRepository<Faculty> FacultyRepository { get; }
        IGenericRepository<Course> CourseRepository { get; }
        IGenericRepository<DisciplineBranch> DisciplineBranchRepository { get; }
        IGenericRepository<Discipline> DisciplineRepository { get; }
        IGenericRepository<Rating> RatingRepository { get; }
        IGenericRepository<RatingType> RatingTypeRepository { get; }
        IGenericRepository<RatingDivision> RatingDivisionRepository { get; }
        IGenericRepository<RatingSystem> RatingSystemRepository { get; }
        IGenericRepository<ProfessorDiscipline> ProfessorDisciplineRepository { get; }
        IGenericRepository<StudentProfessorDiscipline> StudentProfessorDisciplineRepository { get; }
        Task SaveAsync();
        void Save();
    }
}
