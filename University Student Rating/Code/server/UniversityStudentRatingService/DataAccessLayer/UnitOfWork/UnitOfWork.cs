using DataAccessLayer.DbContext;
using DataAccessLayer.Entities;
using DataAccessLayer.Identity;
using Microsoft.AspNet.Identity.EntityFramework;
using Microsoft.AspNet.Identity.Owin;
using Microsoft.Owin;
using Microsoft.Owin.Security;
using DataAccessLayer.Interfaces;
using DataAccessLayer.Repository;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace DataAccessLayer.UnitOfWork
{
    public class UnitOfWork : IUnitOfWork
    {
        private USR_DBEntities dbContext;

        private ApplicationUserManager applicationUserManager;
        private ApplicationRoleManager applicationRoleManager;
        private IGenericRepository<UserProfile> userRepository;
        private IGenericRepository<GenderType> genderTypeRepository;
        private IGenericRepository<Country> countryRepository;
        private IGenericRepository<City> cityRepository;
        private IGenericRepository<University> universityRepository;
        private IGenericRepository<Status> statusRepository;
        private IGenericRepository<Faculty> facultyRepository;
        private IGenericRepository<Course> courseRepository;
        private IGenericRepository<DisciplineBranch> disciplineBranchRepository;
        private IGenericRepository<Discipline> disciplineRepository;
        private IGenericRepository<Rating> ratingRepository;
        private IGenericRepository<RatingType> ratingTypeRepository;
        private IGenericRepository<RatingDivision> ratingDivisionRepository;
        private IGenericRepository<RatingSystem> ratingSystemRepository;
        private IGenericRepository<ProfessorDiscipline> professorDisciplineRepository;
        private IGenericRepository<StudentProfessorDiscipline> studentProfessorDisciplineRepository;


        public UnitOfWork(IOwinContext context)
        {
            dbContext = context.Get<USR_DBEntities>();
            applicationUserManager = context.Get<ApplicationUserManager>();
            applicationRoleManager = context.Get<ApplicationRoleManager>();
            userRepository = new GenericRepository<UserProfile>(dbContext);
            genderTypeRepository = new GenericRepository<GenderType>(dbContext);
            countryRepository = new GenericRepository<Country>(dbContext);
            cityRepository = new GenericRepository<City>(dbContext);
            universityRepository = new GenericRepository<University>(dbContext);
            statusRepository = new GenericRepository<Status>(dbContext);
            facultyRepository = new GenericRepository<Faculty>(dbContext);
            courseRepository = new GenericRepository<Course>(dbContext);
            disciplineBranchRepository = new GenericRepository<DisciplineBranch>(dbContext);
            disciplineRepository = new GenericRepository<Discipline>(dbContext);
            ratingRepository = new GenericRepository<Rating>(dbContext);
            ratingTypeRepository = new GenericRepository<RatingType>(dbContext);
            ratingDivisionRepository = new GenericRepository<RatingDivision>(dbContext);
            ratingSystemRepository = new GenericRepository<RatingSystem>(dbContext);
            professorDisciplineRepository = new GenericRepository<ProfessorDiscipline>(dbContext);
            studentProfessorDisciplineRepository = new GenericRepository<StudentProfessorDiscipline>(dbContext);
        }

        public ApplicationUserManager ApplicationUserManager
        {
            get { return applicationUserManager; }
        }
        public ApplicationRoleManager ApplicationRoleManager
        {
            get { return applicationRoleManager; }
        }
        public IGenericRepository<UserProfile> UserRepository
        {
            get { return userRepository; }
        }
        public IGenericRepository<GenderType> GenderTypeRepository
        {
            get { return genderTypeRepository; }
        }
        public IGenericRepository<Country> CountryRepository
        {
            get { return countryRepository; }
        }
        public IGenericRepository<City> CityRepository
        {
            get { return cityRepository; }
        }
        public IGenericRepository<University> UniversityRepository
        {
            get { return universityRepository; }
        }
        public IGenericRepository<Status> StatusRepository
        {
            get { return statusRepository; }
        }
        public IGenericRepository<Faculty> FacultyRepository
        {
            get { return facultyRepository; }
        }
        public IGenericRepository<Course> CourseRepository
        {
            get { return courseRepository; }
        }
        public IGenericRepository<DisciplineBranch> DisciplineBranchRepository
        {
            get { return disciplineBranchRepository; }
        }
        public IGenericRepository<Discipline> DisciplineRepository
        {
            get { return disciplineRepository; }
        }
        public IGenericRepository<Rating> RatingRepository
        {
            get { return ratingRepository; }
        }
        public IGenericRepository<RatingType> RatingTypeRepository
        {
            get { return ratingTypeRepository; }
        }
        public IGenericRepository<RatingDivision> RatingDivisionRepository
        {
            get { return ratingDivisionRepository; }
        }
        public IGenericRepository<RatingSystem> RatingSystemRepository
        {
            get { return ratingSystemRepository; }
        }
        public IGenericRepository<ProfessorDiscipline> ProfessorDisciplineRepository
        {
            get { return professorDisciplineRepository; }
        }

        public IGenericRepository<StudentProfessorDiscipline> StudentProfessorDisciplineRepository
        {
            get { return studentProfessorDisciplineRepository; }
        }

        #region
        public async Task SaveAsync()
        {
            await dbContext.SaveChangesAsync();
        }
        public void Save()
        {
            dbContext.SaveChanges();
        }

        public void Dispose()
        {
            Dispose(true);
            GC.SuppressFinalize(this);
        }
        private bool disposed = false;

        public virtual void Dispose(bool disposing)
        {
            if (!this.disposed)
            {
                if (disposing)
                {
                    userRepository.Dispose();
                }
                this.disposed = true;
            }
        }
        #endregion
    }
}
