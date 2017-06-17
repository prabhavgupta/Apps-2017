using DataAccessLayer.DbContext;
using ServiceLogicLayer.DTO;
using ServiceLogicLayer.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ServiceLogicLayer.Interfaces
{
    public interface IUserService : IDisposable
    {
        IEnumerable<UserProfile> GetAll();
        UserProfile GetById(int id);
        IEnumerable<UserProfile> GetStudentUserProfiles(int universityId, int facultyId, int courseId);
        IEnumerable<UserProfile> GetStudentUserProfiles(int universityId, int facultyId, int courseId, int professorId, int discipineId);
        AuthUserDTO GetAuthUserProfile(string userName);
        bool SaveStudentProfessorDisciplines(SaveStudentProfessorDiscModel model);
        bool SaveProfessorDisciplines(SaveProfessorDisciplinesModel model);
        bool UpdateUserProfilePhotoPath(string userName, string photoPath);
    }
}
