using DataAccessLayer.DbContext;
using DataAccessLayer.Entities;
using DataAccessLayer.Interfaces;
using ServiceLogicLayer.Interfaces;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Microsoft.Owin.Security;
using Microsoft.AspNet.Identity;
using Microsoft.AspNet.Identity.Owin;
using ServiceLogicLayer.DTO;
using AutoMapper;
using ServiceLogicLayer.Models;

namespace ServiceLogicLayer.Services
{
    public class UserService : IUserService
    {
        IUnitOfWork Database { get; set; }

        public UserService(IUnitOfWork uow)
        {
            Database = uow;
        }

        public IEnumerable<UserProfile> GetAll()
        {
            return Database.UserRepository.GetAll();
        }

        public UserProfile GetById(int id)
        {
            return Database.UserRepository.Get(id);
        }

        public IEnumerable<UserProfile> GetStudentUserProfiles(int universityId, int facultyId, int courseId)
        {
            return Database.UserRepository.Find(u => u.UniversityId == universityId && u.FacultyId == facultyId && u.CourseId == courseId);
        }

        public IEnumerable<UserProfile> GetStudentUserProfiles(int universityId, int facultyId, int courseId, int professorId, int discipineId)
        {
            List<StudentProfessorDiscipline> spds = Database.StudentProfessorDisciplineRepository.Find(spd => spd.ProfessorDiscipline.ProfessorId == professorId &&
                spd.ProfessorDiscipline.DisciplineId == discipineId && spd.UserProfile.UniversityId == universityId && spd.UserProfile.FacultyId == facultyId && spd.UserProfile.CourseId == courseId).ToList();
            return spds.Select(spd => spd.UserProfile);
        }

        public AuthUserDTO GetAuthUserProfile(string userName)
        {
            ApplicationUser user = Database.ApplicationUserManager.FindByName(userName);
            if (user != null)
            {
                user.UserProfile = Database.UserRepository.Get(user.UserProfileId);
                return Mapper.Map<AuthUserDTO>(user);
            }
            return null;
        }

        public bool SaveStudentProfessorDisciplines(SaveStudentProfessorDiscModel model)
        {
            try
            {
                List<StudentProfessorDiscipline> oldStudentProfessorDisciplines = Database.StudentProfessorDisciplineRepository.Find(spd => spd.StudentId == model.StudentId).ToList();
                foreach (var spd in oldStudentProfessorDisciplines)
                {
                    Database.StudentProfessorDisciplineRepository.Delete(spd.Id);
                }
                foreach (var spd in model.StudentProfessorDisciplines)
                {
                    Database.StudentProfessorDisciplineRepository.Create(spd);
                }
                Database.Save();
                return true;
            }
            catch
            {
                return false;
            }
        }

        public bool SaveProfessorDisciplines(SaveProfessorDisciplinesModel model)
        {
            try
            {
                List<ProfessorDiscipline> oldProfessorDisciplines = Database.ProfessorDisciplineRepository.Find(pd => pd.ProfessorId == model.ProfessorId).ToList();
                foreach (var pd in oldProfessorDisciplines)
                {
                    List<StudentProfessorDiscipline> oldStudentProfessorDisciplines = Database.StudentProfessorDisciplineRepository.Find(spd => spd.ProfessorDisciplineId == pd.Id).ToList();
                    foreach (var spd in oldStudentProfessorDisciplines)
                    {
                        Database.StudentProfessorDisciplineRepository.Delete(spd.Id);
                    }
                    Database.ProfessorDisciplineRepository.Delete(pd.Id);
                }
                foreach (var pd in model.ProfessorDisciplines)
                {
                    Database.ProfessorDisciplineRepository.Create(pd);
                }
                Database.Save();
                return true;
            }
            catch
            {
                return false;
            }
        }

        public bool UpdateUserProfilePhotoPath(string userName, string photoPath)
        {
            ApplicationUser user = Database.ApplicationUserManager.FindByName(userName);
            if (user != null)
            {
                UserProfile profile = Database.UserRepository.Get(user.UserProfileId);
                profile.PhotoPath = photoPath;
                Database.Save();
                return true;
            }
            return false;
        }
        public void Dispose()
        {
            Database.Dispose();
        }
    }
}
